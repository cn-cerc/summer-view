package cn.cerc.ui.grid;

import java.util.Optional;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.DataSetSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.AbstractField.BuildUrl;
import cn.cerc.ui.fields.ExpendField;
import cn.cerc.ui.vcl.UIUrl;

public class PhoneLine extends UIComponent implements DataSetSource {
    private DataSetSource source;
    private boolean table = false;
    private String style;
    private ExpendField expender;
    private boolean hidden = false;
    private boolean role = false;

    public PhoneLine(DataGrid owner) {
        super(owner);
        this.source = owner;
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
        DataRow record = getDataSet().map(ds -> ds.current()).orElseThrow();
        html.print("<tr");
        if (this.expender != null || this.hidden) {
            html.print(" style='display: none;'");
            if(this.expender != null) {
                html.print(String.format(" role=\"%s\"", expender.getHiddenId()));
            }
        }
        html.print(">");
        for (UIComponent child : this) {
            AbstractField field = (AbstractField) child;
            html.print("<td");
            if (this.getComponents().size() == 1) {
                html.print(" colspan=2");
            }
            html.print(">");

            String name = field.getShortName();
            if (!"".equals(name))
                html.print(name + ": ");
            BuildUrl build = field.getBuildUrl();
            if(this.role) {
                html.print("<span role='%s'>", field.getField());
            }
            if (build != null) {
                UIUrl url = new UIUrl(null);
                build.buildUrl(record, url);
                url.setText(field.getText()).output(html);
            } else {
                html.print(field.getText());
            }
            if(this.role) {
                html.print("</span>");
            }
            html.print("</td>");
        }
        html.print("</tr>");
    }

    public void outputListString(HtmlWriter html) {
        html.print("<section");
        if(this.hidden) {
            html.print(" style='display: none;' ");
        }
        html.print(">");
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
            if(this.role) {
                html.print("<span role='%s'>", field.getField());
            }
            if (build != null) {
                UIUrl url = new UIUrl(null);
                build.buildUrl(getDataSet().map(ds -> ds.current()).orElseThrow(), url);
                url.setText(field.getText()).output(html);
            } else {
                html.print(field.getText());
            }
            if(this.role) {
                html.print("</span>");
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
    
    public PhoneLine setRole(boolean role) {
        this.role = role;
        return this;
    }
    
    public PhoneLine addHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public void setExpender(ExpendField expender) {
        this.expender = expender;
    }

    /**
     * 
     * @return 返回数据集
     */
    @Override
    public Optional<DataSet> getDataSet() {
        return source.getDataSet();
    }

    @Deprecated
    public void addField(AbstractField child) {
        this.addComponent(child);
    }
}
