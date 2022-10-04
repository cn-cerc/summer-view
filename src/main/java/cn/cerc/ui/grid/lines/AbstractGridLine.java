package cn.cerc.ui.grid.lines;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.AbstractField.BuildUrl;
import cn.cerc.ui.grid.RowCell;
import cn.cerc.ui.vcl.UIUrl;

public abstract class AbstractGridLine extends UIComponent implements DataSource {
    protected DataSource source;
    private List<AbstractField> fields = new ArrayList<>();
    private List<RowCell> cells = new ArrayList<>();
    private boolean visible = true;

    public AbstractGridLine(UIComponent owner) {
        super(owner);
        // 查找最近的数据源
        UIComponent root = owner;
        while (root != null) {
            if (root instanceof DataSource) {
                this.source = (DataSource) root;
                break;
            }
            root = root.getOwner();
        }
    }

    @Override
    public UIComponent addComponent(UIComponent component) {
        super.addComponent(component);
        if (component instanceof AbstractField) {
            AbstractField field = (AbstractField) component;
            field.setVisible(false);
        }
        return this;
    }

    @Override
    public DataSet dataSet() {
        return source.dataSet();
    }

//    @Deprecated
//    public final DataSet getDataSet() {
//        return dataSet();
//    }

    public abstract void output(HtmlWriter html, int lineNo);

    public List<AbstractField> getFields() {
        return fields;
    }

    public RowCell getCell(int index) {
        return cells.get(index);
    }

    protected List<RowCell> getCells() {
        return cells;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public interface IOutputOfGridLine {
        void outputOfGridLine(HtmlWriter html);
    }

    protected void outputCell(HtmlWriter html, AbstractField field) {
        if (field instanceof IOutputOfGridLine) {
            ((IOutputOfGridLine) field).outputOfGridLine(html);
        } else {
            BuildUrl build = field.getBuildUrl();
            if (build != null) {
                UIUrl url = new UIUrl(null);
                build.buildUrl(this.current(), url);
                url.setText(field.getText()).output(html);
            } else {
                html.print(field.getText());
            }
        }
    }
}
