package cn.cerc.ui.ssr.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

/**
 * 使用 attachData 方法添加附加数据时需把值添加到key
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("List附加数据")
public class VuiListValue extends VuiComponent implements ICommonSupplierList, IBinders {
    private static final Logger log = LoggerFactory.getLogger(VuiMapValue.class);
    private List<String> items = new ArrayList<>();
    private Binders binders = new Binders();
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

    public VuiListValue selected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitProperties:
            IVuiEnvironment environment = canvas().environment();
            if (environment == null) {
                log.error("visualPage 为 null，此组件无法工作");
                break;
            }
            Map<String, Object> map = environment.getAttachData(this.getClass());
            for (var key : map.keySet())
                items.add(key);
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
