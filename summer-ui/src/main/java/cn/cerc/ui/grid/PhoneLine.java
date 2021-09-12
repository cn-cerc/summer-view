package cn.cerc.ui.grid;

import cn.cerc.core.DataSource;
import cn.cerc.core.Record;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.ExpendField;
import cn.cerc.ui.other.BuildUrl;

public class PhoneLine extends UIComponent implements DataSource {
    private DataGrid grid;
    private boolean table = false;
    private String style;
    private ExpendField expender;

    public PhoneLine(DataGrid owner) {
        super(owner);
        this.grid = owner;
    }

    public String getStyle() {
        return style;
    }

    public PhoneLine setStyle(String style) {
        this.style = style;
        return this;
    }

    public boolean isTable() {
        return this.table;
    }

    public PhoneLine setTable(boolean table) {
        this.table = table;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.table) {
            outputTableString(html);
        } else {
            outputListString(html);
        }
    }

    private void outputTableString(HtmlWriter html) {
        Record record = getCurrent();
        html.print("<tr");
        if (this.expender != null) {
            html.print(String.format(" role=\"%s\" style=\"display: none;\"", expender.getHiddenId()));
        }
        html.print(">");
        for (UIComponent child : this) {
            AbstractField field = (AbstractField) child;
            html.print("<td");
            if (this.getComponents().size() == 1) {
                html.print(" colspan=2");
            }
            html.print(">");

            BuildUrl build = field.getBuildUrl();
            if (build != null) {
                String name = field.getShortName();
                if (!"".equals(name)) {
                    html.print(name + ": ");
                }
                UrlRecord url = new UrlRecord();
                build.buildUrl(record, url);
                if (!"".equals(url.getUrl())) {
                    html.println("<a href=\"%s\">", url.getUrl());
                    html.print(field.getText());
                    html.println("</a>");
                } else {
                    html.println(field.getText());
                }
            } else {
                outputColumn(field, html);
            }

            html.print("</td>");
        }
        html.print("</tr>");
    }

    public void outputListString(HtmlWriter html) {
        html.print("<section>");
        for (UIComponent child : this) {
            AbstractField field = (AbstractField) child;
            html.print("<span");
            if (field.getCSSClass_phone() != null) {
                html.print(String.format(" class=\"%s\"", field.getCSSClass_phone()));
            }
            html.print(">");
            BuildUrl build = field.getBuildUrl();
            if (build != null) {
                UrlRecord url = new UrlRecord();
                build.buildUrl(getCurrent(), url);
                if (!"".equals(url.getUrl())) {
                    html.println("<a href=\"%s\">", url.getUrl());
                    outputColumn(field, html);
                    html.println("</a>");
                } else {
                    html.println(field.getText());
                }
            } else {
                outputColumn(field, html);
            }
            html.print("</span>");
        }
        html.print("</section>");
    }

    private void outputColumn(AbstractField field, HtmlWriter html) {
        String name = field.getShortName();
        if (!"".equals(name)) {
            html.print(name + ": ");
        }
        html.print(field.getText());
    }

    public PhoneLine addItem(AbstractField... fields) {
        for (AbstractField field : fields) {
            addComponent(field);
        }
        return this;
    }

    public ExpendField getExpender() {
        return expender;
    }

    public void setExpender(ExpendField expender) {
        this.expender = expender;
    }

    @Override
    public boolean isReadonly() {
        return grid.isReadonly();
    }

    @Override
    public Record getCurrent() {
        return grid.getCurrent();
    }

    @Deprecated
    public void addField(AbstractField child) {
        this.addComponent(child);
    }
}
