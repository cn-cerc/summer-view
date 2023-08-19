package cn.cerc.ui.ssr.source;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.PropertiesReader;
import cn.cerc.ui.ssr.PropertiesWriter;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.page.ISupportVisualContainer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("内存数据表")
public class SsrDataSet extends SsrComponent implements ISupportVisualContainer {
    private DataSet dataSet = new DataSet();

    public SsrDataSet() {
        super();
    }

    public SsrDataSet(UIComponent owner) {
        super(owner);
    }

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        reader.read(dataSet);
    }

    @Override
    public void writeProperties(PropertiesWriter writer) {
        writer.write(this);
        writer.write(dataSet);
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);
        var grid = new EditorGrid(content, this);
        for (var field : dataSet.fields()) {
            var title = field.name();
            if (Utils.isEmpty(title))
                title = field.code();
            var width = field.dataType().getLength();
            grid.addColumn(title, field.code(), width);
        }
        for (var row : dataSet)
            grid.addRow().copyValues(row);
        grid.build(pageCode);
    }

    @Override
    public String getIdPrefix() {
        return "dataSet";
    }

}
