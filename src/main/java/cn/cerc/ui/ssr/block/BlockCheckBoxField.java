package cn.cerc.ui.ssr.block;

import java.util.function.Supplier;

import javax.persistence.Column;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockCheckBoxField implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String field;
    @Column
    String valueField;
    Supplier<String> callBackValue;

    public BlockCheckBoxField(String field) {
        super();
        this.field = field;
        init();
    }

    public BlockCheckBoxField(String field, String valueField) {
        super();
        this.field = field;
        this.valueField = valueField;
        init();
    }

    public BlockCheckBoxField(String field, Supplier<String> callBackValue) {
        super();
        this.field = field;
        this.callBackValue = callBackValue;
        init();
    }

    public void init() {
        block.display(1);
        block.option("_valueField", "");
        block.option("_callBackValue", "");
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        String valField = Utils.isEmpty(valueField) ? field : valueField;
        block.text(String.format(
                """
                        <div role='checkbox'>
                                <input type="checkbox" name="%s" ${if _callBackValue}value="callback(callBackValue)"${else}value="${%s}"${endif} />
                            </div>
                        """,
                field, valField));
        block().option("_callBackValue", callBackValue != null ? "1" : "");
        if (callBackValue != null)
            block().onCallback("callBackValue", callBackValue);

        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    public BlockCheckBoxField value(Supplier<String> value) {
        callBackValue = value;
        return this;
    }

}
