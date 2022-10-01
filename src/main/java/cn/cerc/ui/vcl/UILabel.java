package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UILabel extends UIComponent implements IHtml {
    private String text;

    public UILabel() {
        this(null);
    }

    public UILabel(UIComponent component) {
        super(component);
        this.setRootLabel("label");
    }

    public String getFor() {
        return (String) this.getCssProperty("for");
    }

    public UILabel setFor(String focusTarget) {
        this.setCssProperty("for", focusTarget);
        return this;
    }

    @Deprecated
    public void setFocusTarget(String focusTarget) {
        this.setFor(focusTarget);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (this.text != null)
            html.print(this.text);
        super.endOutput(html);
    }

    /**
     * 请改使用 getText
     * 
     * @return 返回 text 值
     */
    @Deprecated
    public String getCaption() {
        return text;
    }

    /**
     * 请改使用 setText
     * 
     * @param caption 设置 text 的值
     */
    @Deprecated
    public void setCaption(String caption) {
        this.text = caption;
    }

    public String getText() {
        return text;
    }

    public UILabel setText(String text) {
        this.text = text;
        return this;
    }

}
