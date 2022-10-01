package cn.cerc.ui.panels;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.columns.IArrayColumn;
import cn.cerc.ui.columns.IColumn;
import cn.cerc.ui.columns.IDataColumn;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.style.IEditPanelStyle;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIDiv;
import cn.cerc.ui.vcl.UIForm;
import cn.cerc.ui.vcl.ext.UIButtonSubmit;

public class UIAppendPanel extends UIComponent implements IEditPanelStyle {
    private UIForm uiform;
    private UIButton submit;
    private HttpServletRequest request;
    private String submitValue;
    private DataRow current = new DataRow();
    private UIComponent inputPanel;
    private String title;
    private IForm form;

    public UIAppendPanel(UIComponent owner) {
        super(owner);
        if (this.getOrigin() instanceof IForm) {
            form = (IForm) this.getOrigin();
            this.request = form.getRequest();
        }
        uiform = new UIForm(this);
        uiform.setCssClass("appendPanel");
        this.inputPanel = new UIComponent(uiform);
        submit = new UIButtonSubmit(uiform.getBottom());
        submit.setText("保存");
        this.title = "增加";
    }

    @Override
    public void output(HtmlWriter html) {
        if (!form.getClient().isPhone()) {
            UIDiv div = new UIDiv();
            div.setCssClass("panelTitle");
            div.setText(this.getTitle());
            div.output(html);
        }

        uiform.beginOutput(html);

        for (UIComponent component : inputPanel) {
            if (component instanceof IDataColumn) {
                IDataColumn column = (IDataColumn) component;
                if (column.isHidden()) {
                    column.setRecord(current);
                    column.outputLine(html);
                }
            }
        }

        html.print("<ul>");
        for (UIComponent component : inputPanel) {
            if (component instanceof IColumn) {
                if (component instanceof IDataColumn) {
                    IDataColumn column = (IDataColumn) component;
                    if (!column.isHidden()) {
                        html.print("<li>");
                        column.setRecord(current);
                        column.outputLine(html);
                        html.print("</li>");
                    }
                } else {
                    IColumn column = (IColumn) component;
                    html.print("<li>");
                    column.outputLine(html);
                    html.print("</li>");
                }
            }
        }
        html.print("</ul>");

        uiform.endOutput(html);
    }

    public void setAction(String action) {
        uiform.setAction(action);
    }

    public String readAll() {
        submitValue = request.getParameter(submit.getName());
        if (!Utils.isEmpty(submitValue)) {
            for (UIComponent component : this.inputPanel) {
                if (component instanceof IArrayColumn) {
                    IArrayColumn column = (IArrayColumn) component;
                    String[] values = request.getParameterValues(column.getCode());
                    if (values == null) {
                        if (!column.isReadonly()) {
                            current.setValue(column.getCode(), "");
                        }
                    } else {
                        current.setValue(column.getCode(), String.join(",", values));
                    }
                } else if (component instanceof IDataColumn) {
                    IDataColumn column = (IDataColumn) component;
                    if (!column.isReadonly()) {
                        String val = request.getParameter(column.getCode());
                        current.setValue(column.getCode(), val == null ? "" : val);
                    }
                }
            }
        }
        return submitValue;
    }

    @Override
    public UIComponent addComponent(UIComponent component) {
        if (component instanceof IColumn) {
            this.inputPanel.addComponent(component);
        } else {
            super.addComponent(component);
        }
        return this;
    }

    public UIComponent getInputPanel() {
        return inputPanel;
    }

    public void setInputPanel(UIComponent inputPanel) {
        this.inputPanel = inputPanel;
    }

    public DataRow current() {
        return this.current;
    }

    public void setCurrent(DataRow current) {
        this.current = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UIForm getUiform() {
        return uiform;
    }

    public UIButton getSubmit() {
        return submit;
    }
}
