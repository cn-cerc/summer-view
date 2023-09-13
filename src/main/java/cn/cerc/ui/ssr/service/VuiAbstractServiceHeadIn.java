package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class VuiAbstractServiceHeadIn<T extends ISupportServiceField> extends VuiAbstractEntityContainer<T>
        implements ISupportServiceDataIn {

    @SuppressWarnings("unchecked")
    public List<T> fields() {
        return getCommponetsByClass((Class<T>) getSupportClass());
    }

    @Override
    public Set<Field> entityFields() {
        LinkedHashSet<Field> result = new LinkedHashSet<>();
        if (getOwner() instanceof ISupplierEntityFields entityFields) {
            result.addAll(entityFields.entityFields());
        }
        return result;
    }

}
