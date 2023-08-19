package cn.cerc.ui.ssr.core;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.ui.core.UIComponent;

@Description("控件")
public class SsrControl extends SsrComponent {
    @Column
    protected static final boolean visual = true;

    public SsrControl() {
        super();
    }

    public SsrControl(UIComponent owner) {
        super(owner);
    }

    @Override
    public ObjectNode config() {
        var config = super.config();
        if (!config.has("v_width"))
            config.put("v_width", 50);
        if (!config.has("v_height"))
            config.put("v_height", 50);
        return config;
    }

}
