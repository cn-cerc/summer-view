package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UILabel;
import cn.cerc.ui.vcl.UIText;

public class RangeField extends AbstractField {

    public RangeField(UIComponent dataView, String name) {
        super(dataView, name, "_range_", 0);
        this.setIcon("images/select-pic.png");
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.isHidden()) {
            super.output(html);
        } else {
            new UILabel(null).setFor(this.getId()).setText(this.getName() + "：").output(html);
            this.forEach(item -> item.output(html));
        }
    }

    @Override
    public RangeField addChild(UIComponent component) {
        if (this.getComponentCount() == 1)
            super.addChild(new UIText(this).setText("-"));
        if (component instanceof AbstractField)
            ((AbstractField) component).setCSSClass_phone("price");
        super.addChild(component);
        return this;
    }

    @Override
    public void updateField() {
        AbstractField child = null;
        for (UIComponent component : this.getComponents()) {
            if (component instanceof AbstractField) {
                child = (AbstractField) component;
                child.updateField();
            }
        }
    }

}
