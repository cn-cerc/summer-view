package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.ui.ssr.editor.SsrMessage;

public abstract class VuiAbstractServiceBodyIn<T extends ISupportServiceField> extends VuiAbstractEntityContainer<T>
        implements ISupportServiceHandler {

    private DataSet dataIn;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> fields() {
        return getCommponetsByClass((Class<T>) getSupportClass());
    }

    protected void validate() throws DataValidateException {
        List<T> fields = fields();
        for (DataRow row : dataIn) {
            for (T field : fields) {
                if (field.required() && !row.hasValue(field.field())) {
                    throw new DataValidateException(String.format("%s 不能为空", field.title()));
                }
            }
        }
    }

    @Override
    public Set<Field> entityFields() {
        LinkedHashSet<Field> result = new LinkedHashSet<>();
        canvas().getMembers().forEach((id, component) -> {
            if (component instanceof VuiAbstractEntityContainer<?> container
                    && (component instanceof ISupplierEntityOpen || component instanceof VuiEntityQuery)) {
                result.addAll(container.entityFields());
            }
        });
        return result;
    }

}