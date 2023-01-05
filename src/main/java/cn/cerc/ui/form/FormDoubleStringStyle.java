package cn.cerc.ui.form;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.vcl.UIInput;

public class FormDoubleStringStyle extends FormStyleDefine {
    private UIInput firstInput = new UIInput(null);
    private UIInput lastInput = new UIInput(null);

    public FormDoubleStringStyle(String code, DataRow dataRow) {
        super(code, dataRow);
        firstInput.setName(code);
    }

    public FormDoubleStringStyle(String name, String code, DataRow dataRow) {
        super(name, code, dataRow);
        firstInput.setName(code);
    }

    public FormDoubleStringStyle(String name, String code, int width, DataRow dataRow) {
        super(name, code, width, dataRow);
        firstInput.setName(code);
    }

    public FormDoubleStringStyle setProportion(int prop1, int prop2) {
        this.firstInput.setCssProperty("style", String.format("flex: %s", prop1));
        this.lastInput.setCssProperty("style", String.format("flex: %s", prop2));
        return this;
    }

    public UIInput getFirstInput() {
        return firstInput;
    }

    public UIInput getLastInput() {
        return lastInput;
    }

    @Override
    public void buildHtml() {
        if (Utils.isEmpty(firstInput.getName()) || Utils.isEmpty(lastInput.getName())) {
            throw new RuntimeException("input attribute name can not be empty.");
        }
        firstInput.setValue(dataRow.getString(firstInput.getName()));
        lastInput.setValue(dataRow.getString(lastInput.getName()));

        builder.append(firstInput.toString());
        builder.append(lastInput.toString());
    }

    @Override
    public List<String> getCodes() {
        List<String> list = new ArrayList<>();
        list.add(firstInput.getName());
        list.add(lastInput.getName());
        return list;
    }
}
