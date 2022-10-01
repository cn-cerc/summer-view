package cn.cerc.ui.grid;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.AbstractField.BuildUrl;
import cn.cerc.ui.fields.ExpendField;
import cn.cerc.ui.vcl.UIUrl;

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
        DataRow record = current();
        html.print("<tr");
        if (this.expender != null) {
            html.print(String.format(" role=\"%s\" style=\"display: none;\"", expender.getHiddenId()));
        }
        html.print(">");
        for (UIComponent child : this) {
            AbstractField field = (AbstractField) child;
            html.print("<td");
            if (this.children().size() == 1) {
                html.print(" colspan=2");
            }
            html.print(">");

            String name = field.getShortName();
            if (!"".equals(name))
                html.print(name + ": ");
            BuildUrl build = field.getBuildUrl();
            if (build != null) {
                UIUrl url = new UIUrl(null);
                build.buildUrl(record, url);
                url.setText(field.getText()).output(html);
            } else {
                html.print(field.getText());
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
            String name = field.getShortName();
            if (!"".equals(name))
                html.print(name + ": ");
            BuildUrl build = field.getBuildUrl();
            if (build != null) {
                UIUrl url = new UIUrl(null);
                build.buildUrl(current(), url);
                url.setText(field.getText()).output(html);
            } else {
                html.print(field.getText());
            }
            html.print("</span>");
        }
        html.print("</section>");
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
    public DataRow current() {
        return grid.current();
    }

    @Deprecated
    public void addField(AbstractField child) {
        this.addComponent(child);
    }
}
