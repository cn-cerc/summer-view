package cn.cerc.ui.ssr.page;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.AbstractPage;
import cn.cerc.ui.ssr.core.ISsrMessage;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.PropertiesWriter;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.core.SsrContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;

public class VisualContainer extends SsrContainer<ISupportVisualContainer> {
    private static final Logger log = LoggerFactory.getLogger(VisualContainer.class);
    private LinkedHashMap<String, SsrComponent> team;
    private ISsrMessage onMessage;
    private ISupplierClassList supplierClassList;
    private AbstractPage page;
    private String redirectPage;
    @Column(name = "标题")
    String title = "";
    @Column(name = "页面描述")
    String readme = "";

    public VisualContainer(UIComponent owner) {
        super(owner);
    }

    public String getReadme() {
        return this.readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    /**
     * 返回所有的子级，包括子级的子级节点<br/>
     * 以id为key，若无id则不收录，若id重复则只收录第1个
     * 
     * @return
     */
    public Map<String, SsrComponent> getMembers() {
        if (this.team == null) {
            this.team = new LinkedHashMap<String, SsrComponent>();
            addChildren(team, this);
        }
        return team;
    }

    private void addChildren(LinkedHashMap<String, SsrComponent> map, SsrComponent root) {
        for (var item : root) {
            if (item instanceof SsrComponent ssr) {
                var id = item.getId();
                if (!Utils.isEmpty(id)) {
                    if (!map.containsKey(id)) {
                        ssr.setContainer(this);
                        map.put(id, ssr);
                    }
                }
                if (item.getComponentCount() > 0)
                    addChildren(map, ssr);
            }
        }
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        var grid = new EditorGrid(content, this);
        for (var item : this)
            grid.addRow().setValue("id", item.getId()).setValue("class", item.getClass().getSimpleName());
        grid.addColumn("代码", "id", 20);
        grid.addColumn("类名", "class", 30);
        grid.build(pageCode);
    }

    @Override
    public String getIdPrefix() {
        return "container";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHeader:
            if (msgData instanceof IHeader header)
                header.setPageTitle(this.title);
            break;
        case SsrMessage.InitPage:
            if (msgData instanceof AbstractPage page)
                this.page = page;
            break;
        case SsrMessage.FailOnService:
            if (msgData instanceof String msg)
                page.setMessage(msg);
            break;
        case SsrMessage.SuccessOnService:
            if (msgData instanceof String msg)
                page.setMessage(msg);
            break;
        }
    }

    /**
     * 返回在当前页面以prefix开头的编号
     * 
     * @param prefix 前缀
     * @return
     */
    public String createUid(String prefix) {
        var it = 0;
        var map = new LinkedHashMap<String, SsrComponent>();
        addChildren(map, this);
        for (var itemId : map.keySet()) {
            if (itemId.startsWith(prefix)) {
                var num = itemId.substring(prefix.length(), itemId.length());
                if (Utils.isNumeric(num)) {
                    if (Integer.parseInt(num) > it)
                        it = Integer.parseInt(num);
                }
            }
        }
        return prefix + (it + 1);
    }

    /**
     * 将json还原为组件集合
     * 
     * @param json
     * @return
     */
    public Object setProperties(String json) {
        this.readProperties(new PropertiesReader(json));
        this.team = null;
        return this;
    }

    /**
     * 从内存中取得属性，形成一个树状的json字符串
     * 
     * @return
     */
    public String getProperties() {
        var writer = new PropertiesWriter();
        this.writeProperties(writer);
        return writer.json();
    }

    /** 通知发送 */
    public void sendMessage(Object sender, int msgType, Object msgData, String targetId) {
        // 通知所有成员相互绑定
        this.getMembers().forEach((id, item) -> {
            if (item != this && (targetId == null || "".equals(targetId) || targetId.equals(id)))
                item.onMessage(sender, msgType, msgData, targetId);
        });
        this.onMessage(sender, msgType, msgData, targetId);
        if (onMessage != null)
            onMessage.onMessage(sender, msgType, msgData, targetId);
    }

    /**
     * 取得相应的组件
     * 
     * @param <T>
     * @param memberId
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getMember(String memberId, Class<T> requiredType) {
        if (Utils.isEmpty(memberId) || memberId.equals(this.getId())) {
            if (requiredType.isInstance(this))
                return Optional.of((T) this);
        }
        var obj = getMembers().get(memberId);
        if (obj != null) {
            if (requiredType.isInstance(obj))
                return Optional.of((T) obj);
            else {
                log.error("找到对象, id={}, 但对象类型({})与期望 {} 不符", memberId, obj.getClass().getSimpleName(),
                        requiredType.getSimpleName());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 在此可以插入自定义业务逻辑
     * 
     * @param onMessage
     */
    public void onMessage(ISsrMessage onMessage) {
        this.onMessage = onMessage;
    }

    @Override
    public VisualContainer getContainer() {
        return this;
    }

    public void supplierClassList(ISupplierClassList supplierClassList) {
        this.supplierClassList = supplierClassList;
    }

    public ISupplierClassList supplierClassList() {
        return supplierClassList;
    }

    public void redirectPage(String redirectPage) {
        this.redirectPage = redirectPage;
    }

    public String redirectPage() {
        return redirectPage;
    }

    public Map<String, Object> getAttachData(Class<? extends SsrComponent> clazz) {
        return supplierClassList.getAttachData(clazz);
    }

}
