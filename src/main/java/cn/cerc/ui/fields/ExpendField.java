package cn.cerc.ui.fields;

import cn.cerc.db.core.ClassResource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.other.SearchItem;

public class ExpendField extends AbstractField implements SearchItem {
    private static final ClassResource res = new ClassResource(ExpendField.class, SummerUI.ID);

    private boolean search;
    private String hiddenId = "hidden";

    public ExpendField(UIComponent owner) {
        this(owner, "", "_opera_", 5);
    }

    public ExpendField(UIComponent owner, String name, String field) {
        this(owner, name, field, 0);
    }

    public ExpendField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
        this.setAlign("center");
        this.setCSSClass_phone("right");
    }

    @Override
    public String getText() {
        if (this.search) {
            return this.getName();
        }
        if (getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            getBuildText().outputText(current(), html);
            return html.toString();
        }
        return String.format("<a href=\"javascript:displaySwitch('%d')\">%s</a>", current().dataSet().recNo(), res.getString(1, "展开"));
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.search) {
            html.print("<a href=\"javascript:displaySwitch('%s')\">%s</a>", this.getHiddenId(), this.getName());
        } else {
            super.output(html);
        }
    }

    public boolean isSearch() {
        return search;
    }

    @Override
    public void setSearch(boolean search) {
        this.search = search;
    }

    public String getHiddenId() {
        if (this.search) {
            return hiddenId;
        }
        return "" + current().dataSet().recNo();
    }

    public void setHiddenId(String hiddenId) {
        this.hiddenId = hiddenId;
    }

}
