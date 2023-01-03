package cn.cerc.ui.form;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.vcl.UIInput;

public class FormRadioStyle extends FormStyleDefine {
    private Map<String, String> options = new LinkedHashMap<>();

    public FormRadioStyle(String code, DataRow data) {
        super(code, data);
    }

    @Override
    public void buildHtml() {
        builder.append("<div class='radioList'>");
        int index = 1;
        for (String key : options.keySet()) {
            String value = options.get(key);
            String id = code + index;
            builder.append(String.format("<input type='%s' name='%s'", UIInput.TYPE_RADIO, code));
            if (dataRow.getString(code).equals(key))
                builder.append(" checked");
            builder.append(String.format(" value='%s' id='%s' />", key, id));
            builder.append(String.format("<label for='%s'>%s</label>", id, value));
            index++;
        }
        builder.append("</div>");
    }

    public FormRadioStyle setOptions(LinkedHashMap<String, String> options) {
        for (String key : options.keySet()) {
            String val = options.get(key);
            this.options.put(key, val);
        }
        return this;
    }

    public FormRadioStyle put(String key, String name) {
        this.options.put(key, name);
        return this;
    }

    public FormRadioStyle setEnum(Enum<?>[] items) {
        for (Enum<?> item : items) {
            options.put(String.valueOf(item.ordinal()), item.name());
        }
        return this;
    }

    public static void main(String[] args) {
//        FromRadioStyle radioStyle = new FromRadioStyle("type");
//        Map<String, String> options = new LinkedHashMap<String, String>();
//        options.put("张三", "张三");
//        options.put("李四", "李四");
//        options.put("王五", "王五");
//        radioStyle.setOptions(options);
//        System.out.println(radioStyle.getHtml());
    }

}
