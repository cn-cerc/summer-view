package cn.cerc.ui.fields;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.page.StaticFile;

public class CodeNameField extends AbstractField {
    private static final ClassConfig config = new ClassConfig(CodeNameField.class, SummerUI.ID);

    private String nameField;

    public CodeNameField(UIComponent owner, String name, String field) {
        super(owner, name, field);
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null)
            this.setIcon(impl.getClassProperty(CodeNameField.class, SummerUI.ID, "icon", ""));
        else
            this.setIcon(config.getClassProperty("icon", ""));
    }

    @Override
    public void updateField() {
        super.updateField();
        this.updateValue(getNameField(), getNameField());
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.isHidden()) {
            html.print("<input");
            html.print(" type=\"hidden\"");
            html.print(" name=\"%s\"", this.getId());
            html.print(" id=\"%s\"", this.getId());
            String value = this.getText();
            if (value != null) {
                html.print(" value=\"%s\"", value);
            }
            html.println("/>");
        } else {
            html.print("<label for=\"%s\"", this.getNameField());
            if (this.getMark() != null) {
                html.print(" class='formMark'");
            }
            html.print(">");
            if (this.isShowStar()) {
                new UIStarFlag(null).output(html);
            }
            html.println("<em>%s</em>", this.getName());
            html.println("</label>");

            html.println("<div data-dialog='true'>");
            html.print("<input");
            html.print(" type=\"hidden\"");
            html.print(" name=\"%s\"", this.getId());
            html.print(" id=\"%s\"", this.getId());
            String codeValue = this.getText();
            if (codeValue != null) {
                html.print(" value=\"%s\"", codeValue);
            }
            html.println("/>");

            html.print("<input");
            html.print(" type=\"text\"");
            html.print(" name=\"%s\"", getNameField());
            html.print(" id=\"%s\"", getNameField());
            String nameValue = null;
            if (current() != null)
                nameValue = current().getString(getNameField());
            if (nameValue != null) {
                html.print(" value=\"%s\"", nameValue);
            }
            if (this.readonly()) {
                html.print(" readonly=\"readonly\"");
            }
            if (this.isAutofocus()) {
                html.print(" autofocus");
            }
            if (this.isAutocomplete())
                html.print(" autocomplete=\"on\"");
            else
                html.print(" autocomplete=\"off\"");

            if (this.isRequired()) {
                html.print(" required");
            }
            if (this.getPlaceholder() != null) {
                html.print(" placeholder=\"%s\"", this.getPlaceholder());
            }
            html.println("/>");

            html.print("<span role='suffix-icon'>");
            if (this.getDialog() != null && this.getDialog().isOpen()) {
                html.print("<a href=\"%s\">", getUrl(this.getDialog()));
                html.print("<img src=\"%s\">", CDN.get(StaticFile.getImage(this.getIcon())));
                html.print("</a>");
            }
            html.print("</span>");
            html.println("</div>");
        }
    }

    public String getUrl(DialogField dialog) {
        if (dialog.getDialogfun() == null) {
            throw new RuntimeException("dialogfun is null");
        }

        StringBuilder build = new StringBuilder();
        build.append("javascript:");
        build.append(dialog.getDialogfun());
        build.append("(");

        build.append("'");
        build.append(getId());
        build.append(",");
        build.append(getNameField());
        build.append("'");

        if (dialog.getParams().size() > 0) {
            build.append(",");
        }

        int i = 0;
        for (String param : dialog.getParams()) {
            build.append("'");
            build.append(param);
            build.append("'");
            if (i != dialog.getParams().size() - 1) {
                build.append(",");
            }
            i++;
        }
        build.append(")");

        return build.toString();
    }

    public String getNameField() {
        if (nameField != null) {
            return nameField;
        }
        return this.getField() + "_name";
    }

    public CodeNameField setNameField(String nameField) {
        this.nameField = nameField;
        return this;
    }

}
