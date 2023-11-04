package cn.cerc.ui.fields;

import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.ColumnEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;
import cn.cerc.ui.vcl.UIUrl;

public class StringField extends AbstractField implements IFormatColumn, IOutputOfGridLine {
    private ColumnEditor editor;

    public StringField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public StringField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field);
        this.setWidth(width);
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

    @Override
    public UIComponent setCssProperty(String key, Object value) {
        return this.getContent().setCssProperty(key, value);
    }

    public StringField expand(boolean expand) {
        if (expand) {
            this.setDialog("initExpand");
            var impl = Application.getBean(ImageConfigImpl.class);
            if (impl != null)
                this.setIcon(impl.getClassProperty(StringField.class, SummerUI.ID, "icon", ""));
            else
                this.setIcon(config.getClassProperty("icon", ""));
        }
        return this;
    }
}
