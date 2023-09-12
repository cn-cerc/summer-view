package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiOutputField extends VuiControl implements ISupportServiceField {
    @Column
    String field = "";
    @Column
    String alias = "";
    @Column
    String title = "";
    @Column
    boolean required = false;

    @Override
    public String field() {
        return field;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void field(String field) {
        this.field = field;
    }

    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public void required(boolean required) {
        this.required = required;
    }

}
