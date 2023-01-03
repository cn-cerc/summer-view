package cn.cerc.ui.form;

import java.util.ArrayList;
import java.util.List;

public class UIDialogField {
    private String dialogFunc;
    private String inputId;
    private String text;
    private List<String> params = new ArrayList<>();

    public UIDialogField(String inputId) {
        this.inputId = inputId;
    }

    public UIDialogField(String inputId, String dialogfun) {
        this.inputId = inputId;
        this.dialogFunc = dialogfun;
    }

    @Override
    public String toString() {
        if (getDialogFunc() == null) {
            throw new RuntimeException("dialogfun is null");
        }
        StringBuilder build = new StringBuilder();
        build.append(getDialogFunc());
        build.append("(");
        build.append(String.format("\"%s\"", inputId));

        for (String param : params) {
            build.append(",");
            build.append(String.format("\"%s\"", param));
        }
        build.append(")");
        return build.toString();
    }

    public String getDialogFunc() {
        return dialogFunc;
    }

    public UIDialogField setDialogFunc(String dialogFunc) {
        this.dialogFunc = dialogFunc;
        return this;
    }

    public String getInputId() {
        return inputId;
    }

    public UIDialogField setInputId(String inputId) {
        this.inputId = inputId;
        return this;
    }

    public UIDialogField add(String param) {
        params.add(param);
        return this;
    }

    public String getText() {
        return text;
    }

    public UIDialogField setText(String text) {
        this.text = text;
        return this;
    }
}
