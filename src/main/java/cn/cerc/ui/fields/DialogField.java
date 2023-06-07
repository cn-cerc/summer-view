package cn.cerc.ui.fields;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;

public class DialogField {
    public static final ClassConfig config = new ClassConfig(AbstractField.class, SummerUI.ID);
    private List<String> params = new ArrayList<>();
    private String inputId;
    private String dialogFunc;
    private String icon;
    private boolean show = true;

    public DialogField() {
        super();
    }

    public DialogField(String dialogFunction) {
        this.dialogFunc = dialogFunction;
    }

    public String getUrl() {
        if (dialogFunc == null) {
            throw new RuntimeException("dialogfun is null");
        }

        StringBuilder build = new StringBuilder();
        build.append("javascript:");
        build.append(dialogFunc);
        build.append("(");

        build.append("\"");
        build.append(inputId);
        build.append("\"");
        if (params.size() > 0) {
            build.append(",");
        }

        int i = 0;
        for (String param : params) {
            build.append("\"");
            build.append(param);
            build.append("\"");
            if (i != params.size() - 1) {
                build.append(",");
            }
            i++;
        }
        build.append(")");

        return build.toString();
    }

    public List<String> getParams() {
        return params;
    }

    public DialogField add(String param) {
        params.add(param);
        return this;
    }

    public String getDialogfun() {
        return dialogFunc;
    }

    public DialogField setDialogfun(String dialogfun) {
        this.dialogFunc = dialogfun;
        return this;
    }

    public String getInputId() {
        return inputId;
    }

    public DialogField setInputId(String inputId) {
        this.inputId = inputId;
        return this;
    }

    public DialogField close() {
        this.show = false;
        return this;
    }

    public boolean isOpen() {
        return show;
    }

    public String getIcon() {
        return Utils.isEmpty(icon) ? DialogField.getIconConfig() : icon;
    }

    public DialogField setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public static String getIconConfig() {
        String icon = "";
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null)
            icon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
        else
            icon = config.getClassProperty("icon", "");
        return icon;
    }

    public static void main(String[] args) {
        DialogField obj = new DialogField("showVipInfo");
        obj.setInputId("inputid");
        obj.add("1");
        obj.add("2");
        obj.add("3");
        System.out.println(obj.getUrl());
    }

}
