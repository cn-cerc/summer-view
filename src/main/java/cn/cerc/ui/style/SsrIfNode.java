package cn.cerc.ui.style;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Variant;

public class SsrIfNode extends SsrForeachNode {
    private static final Logger log = LoggerFactory.getLogger(SsrIfNode.class);
    public static final String StartFlag = "if ";
    public static final String EndFlag = "endif";

    public SsrIfNode(String text) {
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
    public String getHtml() {
        var dataRow = this.getTemplate().getDataRow();
        if (dataRow == null)
            return this.getText();

        Variant status = new Variant();
        var text = this.getField().substring(3, this.getField().length());
        if (check(dataRow, status, text, "==", (left, right) -> left.equals(right))
                || check(dataRow, status, text, "<>", (left, right) -> !left.equals(right))
                || check(dataRow, status, text, "!=", (left, right) -> !left.equals(right))
                || check(dataRow, status, text, ">=", (left, right) -> left.compareTo(right) >= 0)
                || check(dataRow, status, text, "<=", (left, right) -> left.compareTo(right) <= 0)
                || check(dataRow, status, text, ">", (left, right) -> left.compareTo(right) > 0)
                || check(dataRow, status, text, "<", (left, right) -> left.compareTo(right) < 0)
                || check(dataRow, status, text, " is empty", (left, right) -> left.equals(right))
                || check(dataRow, status, text, " is not empty", (left, right) -> !left.equals(right))) {
            if (status.getInt() == -1)
                return this.getText();
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
                return this.getText();
            }
            return getChildren(dataRow.getBoolean(field));
        }
    }

    private String getChildren(boolean ifValue) {
        var dataRow = this.getTemplate().getDataRow();
        var sb = new StringBuffer();

        // 将子项依据else分离成2组
        var items1 = new ArrayList<SsrNodeImpl>();
        var items2 = new ArrayList<SsrNodeImpl>();
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
            if (item instanceof SsrValueNode value) {
                String field = value.getField();
                if (dataRow.exists(field))
                    sb.append(dataRow.getString(field));
                else
                    sb.append(value.getText());
            } else
                sb.append(item.getText());
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}