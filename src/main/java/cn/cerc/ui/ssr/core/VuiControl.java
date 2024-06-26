package cn.cerc.ui.ssr.core;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.ui.core.UIComponent;

@Description("控件")
public class VuiControl extends VuiComponent {
    @Column
    protected static final boolean visual = true;

    public VuiControl() {
        super();
    }

    public VuiControl(UIComponent owner) {
        super(owner);
    }

    @Override
    public ObjectNode properties() {
        return super.properties();
    }

    public Optional<String> properties(String key) {
        var node = this.properties().get(key);
        if (node != null)
            return Optional.ofNullable(node.asText());
        else
            return Optional.empty();
    }
}
