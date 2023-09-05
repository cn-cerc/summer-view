package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;
import cn.cerc.mis.math.FunctionIf;

public class SsrIfNode extends SsrContainerNode {
    private static final Logger log = LoggerFactory.getLogger(SsrIfNode.class);
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("if ", "endif",
            (text) -> new SsrIfNode(text));

    public SsrIfNode(String text) {
        super(text);
    }

    private boolean check(SsrBlock block, Variant status, String text, String flag,
            BiFunction<String, String, Boolean> lrEquals) {
        if (block == null)
            return false;
        var arr = text.split(flag);
        if (arr.length == 1 && text.endsWith(flag)) {
            var field = arr[0].trim();
            var value = block.getValue(field);
            if (value.isEmpty()) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            } else
                status.setValue(lrEquals.apply(value.get(), "") ? 1 : 0);
            return true;
        } else if (arr.length == 2 && (arr[0].length()) > 0) {
            var leftField = arr[0];
            var leftValue = block.getValue(leftField);
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
                rightValue = block.getValue(value);
                if (rightValue.isEmpty()) {
                    log.error("not find field: {}", value);
                    status.setValue(-1);
                    return false;
                }
            }

            status.setValue(lrEquals.apply(leftValue.get(), rightValue.get()) ? 1 : 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getHtml(SsrBlock block) {
        if (block == null)
            return this.getText();

        Variant status = new Variant();
        var text = this.getField().substring(3, this.getField().length());
        if (check(block, status, text, "==", FunctionIf.EQ)
                || check(block, status, text, "<>", FunctionIf.NEQ)
                || check(block, status, text, "!=", FunctionIf.NEQ)
                || check(block, status, text, ">=", FunctionIf.GTE)
                || check(block, status, text, "<=", FunctionIf.LTE)
                || check(block, status, text, ">", FunctionIf.GT)
                || check(block, status, text, "<", FunctionIf.LT)
                || check(block, status, text, " is empty", (left, right) -> left.equals(right))
                || check(block, status, text, " is not empty", (left, right) -> !left.equals(right))) {
            if (status.getInt() == -1)
                return this.getText();
            return getChildren(block, status.getInt() == 1);
        } else { // 直接使用 boolean 字段
            String field = text.trim();
            var tmp = false;
            if (field.startsWith("not ")) {
                field = field.substring(4, field.length());
                tmp = true;
            }
            var value = block.getValue(field);
            if (value.isEmpty()) {
                if (block.strict()) {
                    var e = new RuntimeException(String.format("not find field: %s", field));
                    log.error(e.getMessage(), e);
                    return this.getText();
                } else
                    return getChildren(block, false);
            } else {
                if (tmp)
                    return getChildren(block, !(new Variant(value.get()).getBoolean()));
                else
                    return getChildren(block, new Variant(value.get()).getBoolean());
            }
        }

    }

    private String getChildren(SsrBlock block, boolean ifValue) {
        var sb = new StringBuffer();

        // 将子项依据else分离成2组
        var items1 = new ArrayList<ISsrNode>();
        var items2 = new ArrayList<ISsrNode>();
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
            sb.append(item.getHtml(block));
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
