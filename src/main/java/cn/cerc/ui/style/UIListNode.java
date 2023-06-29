package cn.cerc.ui.style;

import java.util.List;

public class UIListNode extends UIForeachNode {
    public static final String StartFlag = "list.begin";
    public static final String EndFlag = "list.end";

    public UIListNode(String text) {
        super(text);
    }

    public String getValue(List<String> list) {
        if (list == null)
            return this.getSourceText();

        var sb = new StringBuffer();
        for (var param : list) {
            for (var item : this.getItems()) {
                if (item instanceof UIValueNode value) {
                    if ("list.item".equals(item.getText()))
                        sb.append(param);
                    else
                        sb.append(value.getSourceText());
                } else
                    sb.append(item.getSourceText());
            }
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}
