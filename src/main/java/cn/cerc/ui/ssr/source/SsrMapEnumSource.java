package cn.cerc.ui.ssr.source;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.UISsrForm;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportVisualContainer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SsrMapEnumSource extends SsrComponent implements IMapSource, ISupportVisualContainer, IBinders {
    private Map<String, String> items = new LinkedHashMap<>();
    private Binders binders = new Binders();

    @Column(name = "加入全部选择项")
    boolean addAll = false;
    @Column(name = "附加数据源")
    String target = "";
    @Column(name = "默认选中")
    private String selected = "";

    @Override
    public String getIdPrefix() {
        return "mapSource";
    }

    @Override
    public Map<String, String> items() {
        return items;
    }

    @Override
    public Optional<String> selected() {
        if (Utils.isEmpty(this.selected))
            return Optional.empty();
        return Optional.ofNullable(this.selected);
    }

    public SsrMapEnumSource selected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);
        EditorForm form = new EditorForm(content, this);
        UISsrForm ssrForm = form.getForm();
        var style = ssrForm.defaultStyle();
        Map<String, Object> map = getContainer().getAttachData(this.getClass());
        Map<String, String> targetMap = map.keySet().stream().collect(Collectors.toMap(t -> t, t -> t));
        ssrForm.addBlock(style.getBoolean("加入全部选择项", "addAll"));
        ssrForm.addBlock(style.getMap("附加数据源", "target")).toMap(targetMap);
        ssrForm.addBlock(style.getString("默认选中", "selected"));
        form.build();
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitProperties:
            var map = this.getContainer().getAttachData(this.getClass());
            var data = map.get(this.target);
            if (data instanceof Map) {
                if (addAll)
                    items.put("", "全部");
                @SuppressWarnings("unchecked")
                var child = (Map<String, String>) data;
                for (var key : child.keySet()) {
                    items.put(key, child.get(key));
                }
            } else if (data instanceof Enum<?>[] enums) {
                if (addAll)
                    items.put("", "全部");
                for (Enum<?> item : enums)
                    items.put(String.valueOf(item.ordinal()), item.name());
            }
            binders.sendMessage(this, SsrMessage.InitMapSourceDone, null, null);
            break;
        }
    }

    @Override
    public Binders binders() {
        return this.binders;
    }

}
