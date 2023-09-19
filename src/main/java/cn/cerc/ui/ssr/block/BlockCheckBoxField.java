package cn.cerc.ui.ssr.block;

import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class BlockCheckBoxField extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "选择";
    @Column
    String field = "";
    @Column
    String valueField = "";
    Supplier<String> callBackValue;

    public BlockCheckBoxField() {
        super(null);
        init();
    }

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

    @Override
    public String getIdPrefix() {
        return "field";
    }

    public BlockCheckBoxField value(Supplier<String> value) {
        callBackValue = value;
        return this;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public ISupportBlock field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public ISupportBlock title(String title) {
        this.title = title;
        return this;
    }

}
