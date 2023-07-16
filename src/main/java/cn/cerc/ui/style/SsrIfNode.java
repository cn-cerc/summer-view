package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;

public class SsrIfNode extends SsrForeachNode {
    private static final Logger log = LoggerFactory.getLogger(SsrIfNode.class);
    public static final ForeachSignRecord Sign = new ForeachSignRecord("if ", "endif", (text) -> new SsrIfNode(text));

    public SsrIfNode(String text) {
        super(text);
    }

    public interface LeftRightEquals {
        boolean execute(String left, String right);
    }

    public boolean check(Variant status, String text, String flag, LeftRightEquals lrEquals) {
        var template = this.getTemplate();
        if (template == null)
            return false;
        var arr = text.split(flag);
        if (arr.length == 1 && text.endsWith(flag)) {
            var field = arr[0].trim();
            var value = this.getValue(field);
            if (value.isEmpty()) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            } else
                status.setValue(lrEquals.execute(value.get(), "") ? 1 : 0);
            return true;
        } else if (arr.length == 2 && (arr[0].length()) > 0) {
            var leftField = arr[0];
            var leftValue = this.getValue(leftField);
            if (leftValue.isEmpty()) {
                log.error("not find field: {}", leftField);
                status.setValue(-1);
                return false;
            }

            var value = arr[1].trim();
            Optional<String> rightValue;
            if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                rightValue = Optional.of(value.substring(1, value.length() - 1));
            } else if (Utils.isNumeric(value))
                rightValue = Optional.of(value);
            else {
                rightValue = this.getValue(value);
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
        } else { // 直接使用 boolean 字段
            var template = this.getTemplate();
            if (template != null) {
                String field = text.trim();
                var tmp = false;
                if (field.startsWith("not ")) {
                    field = field.substring(4, field.length());
                    tmp = true;
                }
                var value = this.getValue(field);
                if (value.isEmpty()) {
                    log.error("not find field: {}", field);
                    return this.getText();
                } else {
                    if (tmp)
                        return getChildren(!(new Variant(value.get()).getBoolean()));
                    else
                        return getChildren(new Variant(value.get()).getBoolean());
                }
            } else {
                return this.getText();
            }
        }

    }

    private String getChildren(boolean ifValue) {
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
        for (var item : items)
            sb.append(item.getHtml());
        return sb.toString();
    }

    private Optional<String> getValue(String field) {
        var list = this.getTemplate().getList();
        var map = this.getTemplate().getMap();
        var dataRow = this.getTemplate().getDataRow();
        var dataSet = this.getTemplate().getDataSet();
        if (list != null && SsrListItemNode.is(field))
            return Optional.ofNullable(this.getTemplate().getForeachList().item());
        if (map != null) {
            if (SsrMapKeyNode.is(field))
                return Optional.ofNullable(this.getTemplate().getForeachMap().key());
            else if (SsrMapValueNode.is(field))
                return Optional.ofNullable(this.getTemplate().getForeachMap().value());
            else if (map.containsKey(field)) {
                if (dataRow != null && dataRow.exists(field))
                    log.warn("map and dataRow exists field: {}", field);
                if (dataSet != null && dataSet.exists(field))
                    log.warn("map and dataSet exists field: {}", field);
                Object val = map.get(field);
                return Optional.ofNullable(val != null ? val.toString() : "");
            }
        }
        if (dataRow != null && dataRow.exists(field)) {
            if (dataSet != null && dataSet.exists(field))
                log.warn("dataRow and dataSet exists field: {}", field);
            return Optional.of(dataRow.getText(field));
        }

        if (dataSet != null && dataSet.exists(field))
            return Optional.of(dataSet.current().getText(field));
        else
            return Optional.empty();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
