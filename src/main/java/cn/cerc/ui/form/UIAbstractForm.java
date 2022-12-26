package cn.cerc.ui.form;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.SearchSource;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.AbstractPage;
import cn.cerc.ui.vcl.UIButton;

public class UIAbstractForm extends UIComponent implements SearchSource {
    private DataSet dataSet;
    protected List<UIAbstractField> fields = new ArrayList<>();
    private String submit;
    private boolean readAll;
    private AbstractPage page;

    public UIAbstractForm(UIComponent owner, AbstractPage page) {
        super(owner);
        this.page = page;
        this.dataSet = new DataSet();
        dataSet.append();
        this.setRootLabel("form");
    }

    public UIAbstractForm setAction(String action) {
        this.setCssProperty("action", action);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        html.println("<ul>");
        for (UIAbstractField field : fields) {
            html.println("<li>");
            field.output(html);
            html.println("</li>");
        }
        html.println("</ul>");
        this.endOutput(html);
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    public UIAbstractForm setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public UIAbstractForm addComponent(UIComponent child) {
        if (child instanceof UIAbstractField) {
            fields.add((UIAbstractField) child);
        } else {
            super.addComponent(child);
        }
        return this;
    }

    public String readAll() {
        if (readAll) {
            return submit;
        }

        submit = page.getRequest().getParameter("opera");
        for (UIAbstractField field : this.fields) {
            field.updateField();
        }

        readAll = true;
        return submit;
    }

    @Override
    public void updateValue(String id, String code) {
        String val = page.getRequest().getParameter(code);
        if (submit != null) {
            dataSet.setValue(code, val);
        } else {
            if (val != null) {
                dataSet.setValue(code, val);
            }
        }
    }

    public UIButton addButton(String text, String name, String value, String type) {
        UIButton button = new UIButton(this);
        button.setText(text);
        button.setName(name);
        button.setType(type);
        button.setValue(value);
        return button;
    }

}
