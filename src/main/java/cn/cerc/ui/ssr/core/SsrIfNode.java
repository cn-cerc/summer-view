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
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("if ", "endif", SsrIfNode::new);

    public SsrIfNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        if (block == null)
            return this.getText();

        Variant status = new Variant();
        String text = this.getField().substring(3);
        if (check(block, status, text, "==", FunctionIf.EQ) || check(block, status, text, "<>", FunctionIf.NEQ)
                || check(block, status, text, "!=", FunctionIf.NEQ) || check(block, status, text, ">=", FunctionIf.GTE)
                || check(block, status, text, "<=", FunctionIf.LTE) || check(block, status, text, ">", FunctionIf.GT)
                || check(block, status, text, "<", FunctionIf.LT)
                || check(block, status, text, " is empty", String::equals)
                || check(block, status, text, " is not empty", (left, right) -> !left.equals(right))) {
            if (status.getInt() == -1)
                return this.getText();
            return getChildren(block, status.getInt() == 1);
        } else { // 直接使用 boolean 字段
            String field = text.trim();
            boolean tmp = false;
            if (field.startsWith("not ")) {
                field = field.substring(4);
                tmp = true;
            }
            Optional<String> value = block.getValue(field);
            if (value.isEmpty()) {
                if (block.strict()) {
                    block.warn(field);
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

    private boolean check(SsrBlock block, Variant status, String text, String flag,
            BiFunction<String, String, Boolean> lrEquals) {
        if (block == null)
            return false;
        String[] arr = text.split(flag);
        if (arr.length == 1 && text.endsWith(flag)) {
            String field = arr[0].trim();
            Optional<String> value = block.getValue(field);
            if (value.isEmpty()) {
                log.error("not find field: {}", field);
                status.setValue(-1);
            } else
                status.setValue(lrEquals.apply(value.get(), "") ? 1 : 0);
            return true;
        } else if (arr.length == 2 && !arr[0].isEmpty()) {
            String leftField = arr[0];
            Optional<String> leftValue = block.getValue(leftField);
            if (leftValue.isEmpty()) {
                log.error("not find field: {}", leftField);
                status.setValue(-1);
                return false;
            }

            String value = arr[1].trim();
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

    private String getChildren(SsrBlock block, boolean ifValue) {
        StringBuilder builder = new StringBuilder();

        // 将子项依据else分离成2组
        ArrayList<ISsrNode> items1 = new ArrayList<>();
        ArrayList<ISsrNode> items2 = new ArrayList<>();
        boolean elseFlag = false;
        for (ISsrNode item : this.getItems()) {
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
        ArrayList<ISsrNode> items = ifValue ? items1 : items2;
        for (var item : items)
            builder.append(item.getHtml(block));
        return builder.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
