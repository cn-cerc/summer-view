package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UIUrl;

public class UISelectDialog extends UIComponent {
    private DialogField dialog;
    private UIUrl url;
    private UIImage image;

    public UISelectDialog(UIComponent owner) {
        super(owner);
        this.dialog = new DialogField();
    }

    public String inputId() {
        return dialog.getInputId();
    }

    public UISelectDialog setInputId(String inputId) {
        this.dialog.setInputId(inputId);
        return this;
    }

    public UISelectDialog setDialog(DialogField dialog) {
        this.dialog = dialog;
        return this;
    }

    public UISelectDialog setDialog(String dialogFunc, String... params) {
        dialog.setDialogfun(dialogFunc);
        for (String param : params)
            this.dialog.add(param);
        return this;
    }

    public UISelectDialog setDialogIcon(String icon) {
        this.dialog.setIcon(icon);
        return this;
    }

    public UIUrl url() {
        if (this.url == null)
            url = new UIUrl(this);
        return this.url;
    }

    public UIImage image() {
        if (this.image == null)
            image = new UIImage(url());
        return this.image;
    }

    @Override
    public void output(HtmlWriter html) {
        url().setHref(dialog.getUrl());
        image().setSrc(dialog.getIcon());
        super.output(html);
    }

    public static void main(String[] args) {
        var text = new UISelectDialog(null).setDialog("selectPart").toString();
        System.out.println(text);
    }

}
