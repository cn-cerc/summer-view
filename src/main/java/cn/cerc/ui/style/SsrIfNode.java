package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;
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

    public boolean check(Variant status, String text, String flag, LeftRightEquals lrEquals) {
        var template = this.getTemplate();
        var arr = text.split(flag);
        if (arr.length == 1 && text.endsWith(flag)) {
            var field = arr[0];
            var value = template.getValue(field);
            if (value.isEmpty()) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            }
            status.setValue(lrEquals.execute(value.get(), "") ? 1 : 0);
            return true;
        } else if (arr.length == 2 && (arr[0].length()) > 0) {
            var leftField = arr[0];
            var leftValue = template.getValue(leftField);
            if (leftValue.isEmpty()) {
                log.error("not find field: {}", leftField);
                status.setValue(-1);
                return false;
            }

            var value = arr[1];
            Optional<String> rightValue;
            if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                rightValue = Optional.of(value.substring(1, value.length() - 1));
            } else if (Utils.isNumeric(value))
                rightValue = Optional.of(value);
            else {
                rightValue = template.getValue(value);
                if (rightValue.isEmpty()) {
                    log.error("not find field: {}", value);
                    status.setValue(-1);
                    return false;
                }
            }

            status.setValue(lrEquals.execute(leftValue.get(), rightValue.get()) ? 1 : 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getHtml() {
        var template = this.getTemplate();
        if (template.getDataRow() == null && template.getMap() == null)
            return this.getText();

        Variant status = new Variant();
        var text = this.getField().substring(3, this.getField().length());
        if (check(status, text, "==", (left, right) -> left.equals(right))
                || check(status, text, "<>", (left, right) -> !left.equals(right))
                || check(status, text, "!=", (left, right) -> !left.equals(right))
                || check(status, text, ">=", (left, right) -> left.compareTo(right) >= 0)
                || check(status, text, "<=", (left, right) -> left.compareTo(right) <= 0)
                || check(status, text, ">", (left, right) -> left.compareTo(right) > 0)
                || check(status, text, "<", (left, right) -> left.compareTo(right) < 0)
                || check(status, text, " is empty", (left, right) -> left.equals(right))
                || check(status, text, " is not empty", (left, right) -> !left.equals(right))) {
            if (status.getInt() == -1)
                return this.getText();
            return getChildren(status.getInt() == 1);
        } else {
            // 直接使用boolean字段
            String field = text;
            var value = template.getValue(field);
            if (value.isEmpty()) {
                log.error("not find field: {}", field);
                return this.getText();
            } else
                return getChildren(new Variant(value.get()).getBoolean());
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
