package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UIUrl;

public class UISelectDialog extends UIComponent {
    private DialogField button;
    private String icon;
    private UIUrl url;
    private UIImage image;

    public UISelectDialog(UIComponent owner) {
        super(owner);
//        this.setRootLabel("span");
        this.icon = AbstractField.config.getClassProperty("icon", "");
        this.button = new DialogField();
    }

    public String inputId() {
        return button.getInputId();
    }

    public UISelectDialog setInputId(String inputId) {
        this.button.setInputId(inputId);
        return this;
    }

    public String icon() {
        return icon;
    }

    public UISelectDialog setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public UISelectDialog setDialog(String dialogFunc) {
        button.setDialogfun(dialogFunc);
        return this;
    }

    public UISelectDialog setDialog(String dialogFunc, String... params) {
        button.setDialogfun(dialogFunc);
        for (String param : params)
            this.button.add(param);
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
        url().setHref(button.getUrl());
        image().setSrc(icon);
        super.output(html);
    }

    public static void main(String[] args) {
        var text = new UISelectDialog(null).setDialog("selectPart").toString();
        System.out.println(text);
    }

}
