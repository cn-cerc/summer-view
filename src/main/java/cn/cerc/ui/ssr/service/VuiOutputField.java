package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

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
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (getOwner() == sender) {
                if (msgData instanceof DataRow row) {
                    String title = Utils.isEmpty(this.title()) ? this.field() : this.title();
                    if (this.required() && !row.hasValue(this.field()))
                        throw new RuntimeException(String.format("%s不能为空", title));
                }
            }
            break;
        }
    }

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

    public String alias() {
        return alias;
    }

}
