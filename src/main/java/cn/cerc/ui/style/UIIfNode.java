package cn.cerc.ui.style;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Variant;

public class UIIfNode extends UIForeachNode {
    private static final Logger log = LoggerFactory.getLogger(UIIfNode.class);
    public static final String StartFlag = "if ";
    public static final String EndFlag = "endif";

    public UIIfNode(String text) {
        super(text);
    }

    public interface LeftRightEquals {
        boolean execute(String left, String right);
    }

    public boolean check(DataRow dataRow, Variant status, String text, String flag, LeftRightEquals lrEquals) {
        var arr = text.split(flag);
        if (arr.length == 1 && text.endsWith(flag)) {
            var field = arr[0];
            if (!dataRow.exists(field)) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            }
            status.setValue(lrEquals.execute(dataRow.getString(field), "") ? 1 : 0);
            return true;
        } else if (arr.length == 2 && (arr[0].length()) > 0) {
            var field = arr[0];
            var value = arr[1];
            if (!dataRow.exists(field)) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            }
            status.setValue(lrEquals.execute(dataRow.getString(field), value) ? 1 : 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getValue() {
        var dataRow = this.getTemplate().getDataRow();
        if (dataRow == null)
            return this.getSourceText();

        Variant status = new Variant();
        var text = this.getField().substring(3, this.getField().length());
        if (check(dataRow, status, text, "==", (left, right) -> left.equals(right))
                || check(dataRow, status, text, "<>", (left, right) -> !left.equals(right))
                || check(dataRow, status, text, "!=", (left, right) -> !left.equals(right))
                || check(dataRow, status, text, ">=", (left, right) -> left.compareTo(right) >= 0)
                || check(dataRow, status, text, "<=", (left, right) -> left.compareTo(right) <= 0)
                || check(dataRow, status, text, ">", (left, right) -> left.compareTo(right) > 0)
                || check(dataRow, status, text, "<", (left, right) -> left.compareTo(right) < 0)
                || check(dataRow, status, text, " is null", (left, right) -> left.equals(right))
                || check(dataRow, status, text, " is not null", (left, right) -> !left.equals(right))) {
            if (status.getInt() == -1)
                return this.getSourceText();
            return getChildren(status.getInt() == 1);
        } else {
            // 直接使用boolean字段
            String field = text;
            var map = this.getTemplate().getMap();
            if (map != null && map.containsKey(field)) {
                var value = new Variant(map.get(field));
                return getChildren(value.getBoolean());
            }
            if (!dataRow.exists(field)) {
                log.error("not find field: {}", field);
                return this.getSourceText();
            }
            return getChildren(dataRow.getBoolean(field));
        }
    }

    private String getChildren(boolean ifValue) {
        var dataRow = this.getTemplate().getDataRow();
        var sb = new StringBuffer();

        // 将子项依据else分离成2组
        var items1 = new ArrayList<UISsrNodeImpl>();
        var items2 = new ArrayList<UISsrNodeImpl>();
        var elseFlag = false;
        for (var item : this.getItems()) {
            if ("else".equals(item.getField())) {
                elseFlag = true;
                continue;
            }
            if (!elseFlag)
                items1.add(item);
            else
                items2.add(item);
        }
        
        // 根据参数决定是执行1还是执行2
        var items = ifValue ? items1 : items2;
        for (var item : items) {
            if (item instanceof UIValueNode value) {
                String field = value.getField();
                if (dataRow.exists(field))
                    sb.append(dataRow.getString(field));
                else
                    sb.append(value.getSourceText());
            } else
                sb.append(item.getSourceText());
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}
