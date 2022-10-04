package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UITd extends UIComponent implements IHtml {
    private UIText text;

    @Deprecated
    public UITd() {
        this(null);
    }

    public UITd(UIComponent component) {
        super(component);
        this.setRootLabel("td");
        this.text = new UIText(this);
    }

    public int getColspan() {
        return (int) this.getCssProperty("colspan");
    }

    public UITd setColspan(int colspan) {
        this.setCssProperty("colspan", colspan);
        return this;
    }

    public int getRowspan() {
        return (int) this.getCssProperty("rowspan");
    }

    public UITd setRowspan(int rowspan) {
        this.setCssProperty("rowspan", rowspan);
        return this;
    }

    public String getText() {
        return text.getText();
    }

    public UITd setText(String text) {
        this.text.setText(text);
        return this;
    }

}
