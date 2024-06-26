package cn.cerc.ui.ssr.source;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.PropertiesWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.form.ISupportField;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.report.ISupportRpt;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("请求数据行")
@VuiCommonComponent
public class VuiRequestRow extends VuiComponent
        implements ISupplierDataRow, ISupportCanvas, ISupportChart, ISupportXls, ISupportRpt, IBinders {
    private DataRow config = new DataRow();
    private DataRow dataRow = new DataRow();
    private HttpServletRequest request;
    private Binders binders = new Binders();

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        reader.read(config);
    }

    @Override
    public void writeProperties(PropertiesWriter writer) {
        writer.write(this);
        writer.writer(config);
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
        var form = new EditorForm(content, this);
        for (var field : config.fields()) {
            var title = field.name();
            if (Utils.isEmpty(title))
                title = field.code();
            form.addItem(title, field.code(), config.getString(field.code()));
        }
        // 显示可以增加的组件列表
        form.addItem("增加字段", "appendField", "");
        form.build();
    }

    @Override
    public void saveEditor(RequestReader reader) {
        for (var field : config.fields()) {
            reader.getString(field.code()).ifPresent(value -> config.setValue(field.code(), value));
        }
        var appendField = reader.getString("appendField");
        if (appendField.isPresent() && !Utils.isEmpty(appendField.get())) {
            String fieldCode = appendField.get();
            if (!this.config.fields().exists(fieldCode))
                this.config.fields().add(fieldCode);
        }
    }

    @Override
    public String getIdPrefix() {
        return "dataRow";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest reuqest) {
                this.request = reuqest;
                for (var field : config.fields()) {
                    String key = config.getString(field.code());
                    if (Utils.isEmpty(key))
                        key = field.code();
                    dataRow.setValue(field.code(), request.getParameter(key));
                }
                binders.sendMessage(this, SsrMessage.RefreshProperties, dataRow, null);
            }
            break;
        case SsrMessage.appendComponent:
            if (sender instanceof VuiForm && msgData instanceof ISupportField block) {
                String newField = block.fields();
                if (!Utils.isEmpty(newField) && !config.fields().exists(newField))
                    config.fields().add(newField);
            }
            break;
        case SsrMessage.UpdateFieldCode:
            if (sender instanceof VuiForm && msgData instanceof String newField) {
                if (!config.fields().exists(newField))
                    config.fields().add(newField);
            }
            break;
        }
    }

    @Override
    public Binders binders() {
        return binders;
    }
}
