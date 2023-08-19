package cn.cerc.ui.ssr.source;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportVisualContainer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SsrMapValueSource extends SsrComponent implements IMapSource, ISupportVisualContainer, IBinders {
    private Map<String, String> items = new LinkedHashMap<>();
    private String selected = "";
    private Binders binders = new Binders();
    @Column(name = "加入全部选择项")
    boolean addAll = false;

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

    public SsrMapValueSource selected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitProperties:
            Map<String, Object> map = getContainer().getAttachData(this.getClass());
            if (addAll)
                items.put("", "全部");
            for (var key : map.keySet()) {
                var val = map.get(key);
                if (val instanceof String value)
                    items.put(key, value);
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
