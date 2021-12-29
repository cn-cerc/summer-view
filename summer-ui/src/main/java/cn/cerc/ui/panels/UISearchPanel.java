package cn.cerc.ui.panels;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.columns.IArrayColumn;
import cn.cerc.ui.columns.IColumn;
import cn.cerc.ui.columns.IDataColumn;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.style.ISearchPanelStyle;
import cn.cerc.ui.vcl.UIForm;
import cn.cerc.ui.vcl.ext.UIButtonSubmit;

public class UISearchPanel extends UIComponent implements ISearchPanelStyle {
    private UIComponent filterPanel;
    private UIComponent controlPanel;
    private UIButtonSubmit submit;
    private DataRow record;
    private UIForm uiform;

    public UISearchPanel(UIComponent owner) {
        super(owner);
        this.uiform = new UIForm();
        this.record = new DataRow();
        this.filterPanel = new UIComponent(uiform);
        this.controlPanel = new UIComponent(uiform);
        submit = new UIButtonSubmit(uiform.getBottom());
        submit.setText("查询");
    }

    @Override
    public void output(HtmlWriter html) {
        uiform.setCssClass("searchPanel");
        uiform.beginOutput(html);

        html.print("<ul>");
        for (UIComponent component : filterPanel) {
            html.print("<li>");
            if (component instanceof IColumn) {
                IColumn column = ((IColumn) component);
                if (component instanceof IDataColumn) {
                    ((IDataColumn) column).setRecord(record);
                }
                column.outputLine(html);
            } else {
                component.output(html);
            }
            html.print("</li>");
        }
        html.print("</ul>");

        for (UIComponent component : controlPanel) {
            html.print("<div>");
            component.output(html);
            html.print("</div>");
        }

        uiform.endOutput(html);
    }

    public String readAll() {
        if (!(this.getOrigin() instanceof IForm))
            throw new RuntimeException("origin is not IForm");
        HttpServletRequest request = ((IForm) this.getOrigin()).getRequest();
        
        String result = request.getParameter(submit.getName());
        if (!Utils.isEmpty(result)) {
            // 将用户值或缓存值存入到dataSet中
            for (UIComponent component : this.filterPanel) {
                if (component instanceof IArrayColumn) {
                    IArrayColumn column = (IArrayColumn) component;
                    String[] values = request.getParameterValues(column.getCode());
                    if (values == null) {
                        if (!column.isReadonly()) {
                            record.setValue(column.getCode(), "");
                        }
                    } else {
                        record.setValue(column.getCode(), String.join(",", values));
                    }
                } else if (component instanceof IDataColumn) {
                    IDataColumn column = (IDataColumn) component;
                    if (!column.isReadonly()) {
                        String val = request.getParameter(column.getCode());
                        record.setValue(column.getCode(), val == null ? "" : val);
                    }
                }
            }
        }
        return result;
    }

    public UIComponent getControlPanel() {
        return controlPanel;
    }

    public UIComponent getFilterPanel() {
        return filterPanel;
    }

    public UIButtonSubmit getSubmit() {
        return submit;
    }

    @Override
    public UIComponent addComponent(UIComponent component) {
        if (component instanceof IColumn) {
            this.filterPanel.addComponent(component);
        } else {
            super.addComponent(component);
        }
        return this;
    }

    public DataRow getRecord() {
        return record;
    }

    public void setRecord(DataRow record) {
        this.record = record;
    }

    public UIForm getUiform() {
        return uiform;
    }
}
