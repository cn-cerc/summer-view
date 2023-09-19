package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldDefs;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiServiceHeadOut extends VuiAbstractEntityContainer<VuiOutputField> implements ISupportServiceDataOut {

    private DataSet dataOut;
    private Map<String, VuiOutputField> fields;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (getOwner() != sender)
                return;
            dataOut = new DataSet();
            if (msgData instanceof DataSet dataIn) {
                dataOut.head().copyValues(dataIn.current());
                sendMessageToChild(msgType, dataOut.head());
                cleanFields(dataOut.head());
            } else if (msgData instanceof DataRow row) {
                dataOut.head().copyValues(row);
                sendMessageToChild(msgType, dataOut.head());
                cleanFields(dataOut.head());
            }
            break;
        }
    }

    private void cleanFields(DataRow row) {
        FieldDefs fields = row.fields();
        Map<String, VuiOutputField> outputFields = getOutputFields();
        if (!outputFields.keySet().contains("*")) {
            List<String> removeFields = fields.getItems()
                    .stream()
                    .map(FieldMeta::code)
                    .filter(t -> !outputFields.keySet().contains(t))
                    .toList();
            for (String field : removeFields) {
                fields.remove(field);
            }
        }
        for (VuiOutputField field : outputFields.values()) {
            if (!Utils.isEmpty(field.alias())) {
                row.setValue(field.alias(), row.getString(field.field()));
                fields.remove(field.field());
            }
        }
    }

    public Map<String, VuiOutputField> getOutputFields() {
        if (fields == null)
            fields = getCommponetsByClass(VuiOutputField.class).stream()
                    .collect(Collectors.toMap(VuiOutputField::field, t -> t, (o1, o2) -> o1, LinkedHashMap::new));
        return fields;
    }

    @Override
    public Set<Field> entityFields() {
        LinkedHashSet<Field> result = new LinkedHashSet<>();
        if (getOwner() instanceof ISupplierEntityFields entityFields) {
            result.addAll(entityFields.entityFields());
        }
        return result;
    }

    @Override
    public DataSet dataOut() {
        return dataOut;
    }

}
