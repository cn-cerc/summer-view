package cn.cerc.ui.fields;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.ColumnEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UIUrl;

public class DoubleField extends AbstractField implements IFormatColumn, IOutputOfGridLine {
    private ColumnEditor editor;
    private String format = "0.####";
    private String step = "any";

    public DoubleField(UIComponent owner, String title, String field) {
        super(owner, title, field, 4);
        this.setAlign("right");
    }

    public DoubleField(UIComponent owner, String title, String field, int width) {
        super(owner, title, field, width);
        this.setAlign("right");
    }

    @Override
    public String getText() {
        if (getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            getBuildText().outputText(current(), html);
            return html.toString();
        }
        try {
            double val = current().getDouble(this.getField());
            DecimalFormat df = new DecimalFormat(format);
            return df.format(new BigDecimal(Double.toString(val)));
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    @Override
    public Title createTitle() {
        Title title = super.createTitle();
        title.setType("float");
        return title;
    }

    @Override
    public void outputOfGridLine(HtmlWriter html) {
        if (this.readonly()) {
            if (getBuildUrl() != null) {
                UIUrl url = new UIUrl(null);
                getBuildUrl().buildUrl(current(), url);
                if (!"".equals(url.getHref())) {
                    url.setText(getText());
                    url.output(html);
                } else {
                    html.println(getText());
                }
            } else {
                html.print(getText());
            }
        } else {
            html.print(getEditor().format(current()));
        }
    }

    public ColumnEditor getEditor() {
        if (editor == null) {
            editor = new ColumnEditor(this);
        }
        return editor;
    }

    public String getFormat() {
        return format;
    }

    public DoubleField setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public UIComponent setCssProperty(String key, Object value) {
        return this.getContent().setCssProperty(key, value);
    }

    @Override
    public String getHtmType() {
        return UIInput.TYPE_NUMBER;
    }

    public String getStep() {
        return step;
    }

    public DoubleField setStep(String step) {
        this.step = step;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        getContent().setCssProperty("step", step);
        super.output(html);
    }
}
