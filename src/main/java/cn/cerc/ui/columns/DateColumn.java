package cn.cerc.ui.columns;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.IReadonlyOwner;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UILabel;

public class DateColumn extends AbstractColumn implements IDataColumn {
    private UIInput input = new UIInput(this);
    private UIComponent helper;
    private boolean readonly;

    public DateColumn(UIComponent owner) {
        super(owner);
        if (owner instanceof IReadonlyOwner) {
            this.setReadonly(((IReadonlyOwner) owner).readonly());
        }
    }

    public DateColumn(UIComponent owner, String name, String code) {
        super(owner);
        this.setCode(code).setName(name).setSpaceWidth(4);
        if (owner instanceof IReadonlyOwner) {
            this.setReadonly(((IReadonlyOwner) owner).readonly());
        }
    }

    @Override
    public void outputCell(HtmlWriter html) {
        if (this.getOrigin() instanceof IForm) {
            IForm form = (IForm) this.getOrigin();
            if (form.getClient().isPhone()) {
                outputCellPhone(html);
                return;
            }
        }
        outputCellWeb(html);
    }

    private void outputCellPhone(HtmlWriter html) {
        String value = getRecord().getString(this.getCode());
        if (!Utils.isEmpty(value)) {
            value = value.substring(0, 10);
        }
        if (this.readonly) {
            html.print(getName() + "：");
            html.print(value);
        } else {
            UILabel label = new UILabel();
            label.setFor(this.getCode());
            label.setText(getName() + "：");
            label.output(html);

            input.setId(getCode());
            input.setInputType(UIInput.TYPE_DATE);
            input.setReadonly(readonly);
            input.setValue(value);
            input.output(html);
        }
    }

    private void outputCellWeb(HtmlWriter html) {
        String value = getRecord().getString(this.getCode());
        if (!Utils.isEmpty(value)) {
            value = value.substring(0, 10);
        }
        if (this.readonly) {
            html.print(value);
        } else {
            input.setInputType(UIInput.TYPE_DATE);
            input.setValue(value);
            input.output(html);
        }
    }

    @Override
    public void outputLine(HtmlWriter html) {
        String text = getRecord().getString(this.getCode());
        if (!this.isHidden()) {
            UILabel label = new UILabel();
            label.setFor(this.getCode());
            label.setText(getName() + "：");
            label.output(html);
        }

        input.setReadonly(this.isReadonly());
        input.setId(getCode());
        input.setInputType(UIInput.TYPE_DATE);
        if (!Utils.isEmpty(text)) {
            text = text.substring(0, 10);
        }
        input.setValue(text);
        input.output(html);

        if (!this.isHidden()) {
            html.print("<span>");
            if (this.helper != null) {
                helper.output(html);
            }
            html.println("</span>");
        }
    }

    public UIComponent getHelper() {
        if (helper != null)
            helper = new UIComponent(this);
        return helper;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public DateColumn setReadonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    @Override
    public boolean isHidden() {
        return input.isHidden();
    }

    @Override
    public DateColumn setHidden(boolean hidden) {
        input.setHidden(hidden);
        return this;
    }

}
