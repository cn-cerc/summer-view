package cn.cerc.ui.ssr.source;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.PropertiesWriter;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("请求数据集")
public class VuiRequestDataSet extends VuiComponent implements ICommonSupplierDataSet {
    /**
     * key: Request中的请求字段, value: 请求内容与字段名称的对应 </br>
     * request: value: ["A1,B1,C1","A2,B2,C2","A3,B3,C3"] </br>
     * config: value: "field1,field2,field3" </br>
     * 转换后的DataSet: body: [[field1,field2,field3], [A1,B1,C1], [A2,B2,C2],
     * [A3,B3,C3]] </br>
     * 详情请查看测试用例：VuiRequestDataSetTest
     */
    private DataRow config = new DataRow();
    private DataSet dataSet = new DataSet();
    private HttpServletRequest request;

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
        return "dataSet";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest reuqest) {
                this.request = reuqest;
                initDataSet();
            }
            break;
        }
    }

    protected void initDataSet() {
        for (FieldMeta fieldMeta : config.fields()) {
            String name = fieldMeta.code();
            if (Utils.isEmpty(name))
                continue;
            String field = config.getString(name);
            if (Utils.isEmpty(field))
                continue;
            String[] fields = field.split(",");
            String[] values = getValues(name);
            if (Utils.isEmpty(values))
                continue;
            dataSet.first();
            for (String value : values) {
                String[] args = value.split(",");
                if (args.length < fields.length)
                    continue;
                if (dataSet.eof()) // 如果DataSet没有内容了则追加一条
                    dataSet.append();
                for (int i = 0; i < fields.length; i++)
                    dataSet.setValue(fields[i], args[i]);
                dataSet.next();
            }
        }
    }

    protected String[] getValues(String name) {
        return request.getParameterValues(name);
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    protected void config(DataRow config) {
        this.config = config;
    }

}
