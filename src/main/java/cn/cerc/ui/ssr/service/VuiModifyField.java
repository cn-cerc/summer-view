package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiModifyField extends VuiControl implements ISupportUpdate {
    private static final Logger log = LoggerFactory.getLogger(VuiModifyField.class);

    @Column
    String field = "";
    @Column
    String title = "";
    @Column
    boolean required = false;

    private DataRow dataIn;
    private EntityHelper<?> entityHelper;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        if (getOwner() != sender)
            return;
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataRow dataIn)
                this.dataIn = dataIn;
            break;
        case SsrMessage.initEntityHelper:
            if (msgData instanceof EntityHelper<?> entityHelper)
                this.entityHelper = entityHelper;
            break;
        case SsrMessage.RunServiceModify:
            if (dataIn == null)
                throw new RuntimeException("DataIn不能为NULL");
            if (msgData instanceof CustomEntity item) {
                String fieldCode = this.field();
                String title = Utils.isEmpty(this.title()) ? this.field() : this.title();
                if (!this.required() && !dataIn.hasValue(fieldCode))
                    throw new RuntimeException(String.format("%s不能为空", title));
                Field field = entityHelper.fields().get(fieldCode);
                if (field != null) {
                    try {
                        field.set(item, getByType(fieldCode, field.getType()));
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            break;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getByType(String field, Class<T> clazz) {
        if (clazz == String.class)
            return (T) dataIn.getString(field);
        else if (clazz == boolean.class || clazz == Boolean.class)
            return (T) Boolean.valueOf(dataIn.getBoolean(field));
        else if (clazz == int.class || clazz == Integer.class)
            return (T) Integer.valueOf(dataIn.getInt(field));
        else if (clazz == double.class || clazz == Double.class)
            return (T) Double.valueOf(dataIn.getDouble(field));
        else if (clazz == long.class || clazz == Long.class)
            return (T) Long.valueOf(dataIn.getLong(field));
        else if (clazz == Datetime.class)
            return (T) dataIn.getDatetime(field);
        else if (clazz.isEnum())
            return (T) dataIn.getEnum(field, (Class<Enum<?>>) clazz);
        else
            return (T) dataIn.getValue(field);
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

}
