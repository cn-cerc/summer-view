package cn.cerc.ui.ssr.source;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.PropertiesWriter;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.ISupplierDataRow;
import cn.cerc.ui.ssr.form.ISupportForm;
import cn.cerc.ui.ssr.form.VuiForm;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("内存数据行")
public class VuiDataRow extends VuiComponent implements ISupplierDataRow {
    private DataRow dataRow = new DataRow();

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        reader.read(dataRow);
    }

    @Override
    public void writeProperties(PropertiesWriter writer) {
        writer.write(this);
        writer.writer(dataRow);
    }

    @Override
    public DataRow dataRow() {
        return dataRow;
    }

    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);
        var form = new EditorForm(content, this);
        for (var field : dataRow.fields()) {
            var title = field.name();
            if (Utils.isEmpty(title))
                title = field.code();
            form.addItem(title, field.code(), dataRow.getString(field.code()));
        }
        // 显示可以增加的组件列表
        form.addItem("增加字段", "appendField", "");
        form.build();
    }

    @Override
    public void saveEditor(RequestReader reader) {
        super.saveEditor(reader);
        for (var field : dataRow.fields()) {
            reader.getString(field.code()).ifPresent(value -> dataRow.setValue(field.code(), value));
        }
        var appendField = reader.getString("appendField");
        if (appendField.isPresent()) {
            if (!this.dataRow.fields().exists(appendField.get()))
                this.dataRow.fields().add(appendField.get());
        }
    }

    @Override
    public String getIdPrefix() {
        return "dataRow";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.appendComponent:
            if (sender instanceof VuiForm form && msgData instanceof ISupportForm block) {
                String newField = block.fields();
                if (!Utils.isEmpty(newField) && !dataRow.fields().exists(newField))
                    dataRow.fields().add(newField);
            }
            break;
        case SsrMessage.UpdateFieldCode:
            if (sender instanceof VuiForm form && msgData instanceof String newField) {
                if (!dataRow.fields().exists(newField))
                    dataRow.fields().add(newField);
            }
            break;
        }
    }

}
