package cn.cerc.ui.ssr.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiListSupplier extends VuiComponent implements ISupplierList, IBinders {
    private static final Logger log = LoggerFactory.getLogger(VuiMapSupplier.class);
    private List<String> items = new ArrayList<>();
    private Binders binders = new Binders();

    @Column(name = "附加数据源")
    String target = "";
    @Column(name = "默认选中")
    private String selected = "";
    @Column(name = "加入全部选择项")
    boolean addAll = false;

    @Override
    public String getIdPrefix() {
        return "listSource";
    }

    @Override
    public List<String> items() {
        return items;
    }

    @Override
    public Optional<String> selected() {
        if (Utils.isEmpty(this.selected))
            return Optional.empty();
        return Optional.ofNullable(this.selected);
    }

    public VuiListSupplier selected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        IVuiEnvironment environment = canvas().environment();
        if (environment != null) {
            EditorForm form = new EditorForm(content, this);
            form.addProperties(this);
            VuiForm ssrForm = form.getForm();
            var style = ssrForm.defaultStyle();
            Map<String, Object> map = environment.getAttachData(this.getClass());
            Map<String, String> targetMap = map.keySet().stream().collect(Collectors.toMap(t -> t, t -> t));
            if (targetMap.size() == 0)
                targetMap.put("", "暂无附加数据源");
            else
                targetMap.put("", "请选择附加数据源");
            ssrForm.addBlock(style.getString("附加数据源", "target").toMap(targetMap));
            form.build();
        } else {
            super.buildEditor(content, pageCode);
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitProperties:
            var visualPage = this.canvas().environment();
            if (visualPage == null) {
                log.error("visualPage 为 null，此组件无法工作");
                return;
            }
            var map = visualPage.getAttachData(this.getClass());
            var data = map.get(this.target);

            if (data instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> child = (List<String>) data;
                for (var value : child)
                    items.add(value);
            } else if (data instanceof Enum<?>[] enums) {
                for (Enum<?> item : enums)
                    items.add(item.name());
            }
            binders.sendMessage(this, SsrMessage.InitListSourceDone, null, null);
            break;
        }
    }

    @Override
    public Binders binders() {
        return this.binders;
    }

    @Override
    public boolean addAll() {
        return addAll;
    }

}
