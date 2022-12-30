package cn.cerc.ui.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.SearchSource;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.AbstractPage;
import cn.cerc.ui.vcl.UIButton;

public class UIAbstractForm extends UIComponent implements SearchSource {
    private DataSet dataSet;
    private List<UIAbstractField> fields = new ArrayList<>();
    private Map<String, List<UIAbstractField>> groupMap = new LinkedHashMap<String, List<UIAbstractField>>();
    private String currentGroup;
    private String submit;
    private boolean readAll;
    private AbstractPage page;

    public UIAbstractForm(UIComponent owner, AbstractPage page) {
        super(owner);
        this.page = page;
        this.dataSet = new DataSet();
        dataSet.append();
        this.setRootLabel("form");
        this.setCssClass("customForm");
        this.setCssProperty("method", "post");
    }

    public UIAbstractForm setAction(String action) {
        this.setCssProperty("action", action);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!Utils.isEmpty(currentGroup))
            this.setCssClass("customForm groupForm");
        this.beginOutput(html);
        if (Utils.isEmpty(currentGroup)) {
            printList(html, fields);
        } else {
            for (String key : groupMap.keySet()) {
                html.print("<div class='formGroup'>");
                html.print("<div class='groupTitle'>%s</div>", key);
                printList(html, groupMap.get(key));
                html.print("</div>");
            }
        }
        this.endOutput(html);
    }

    private void printList(HtmlWriter html, List<UIAbstractField> fields) {
        html.println("<ul>");
        for (UIAbstractField field : fields) {
            html.print("<li role='col%s'>", field.getOutWidth());
            field.output(html);
            html.println("</li>");
        }
        html.println("</ul>");
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
            UIAbstractField abstractField = (UIAbstractField) child;
            if (Utils.isEmpty(currentGroup))
                fields.add(abstractField);
            else if (groupMap.get(currentGroup) != null)
                groupMap.get(currentGroup).add(abstractField);
            else {
                List<UIAbstractField> fields_ = new ArrayList<>();
                fields_.add(abstractField);
                groupMap.put(currentGroup, fields_);
            }
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
        if (Utils.isEmpty(this.currentGroup))
            for (UIAbstractField field : this.fields) {
                field.updateField();
            }
        else {
            for (String key : groupMap.keySet()) {
                for (UIAbstractField field : groupMap.get(key)) {
                    field.updateField();
                }
            }
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

    public void addGroup(String group) {
        this.currentGroup = group;
    }

}
