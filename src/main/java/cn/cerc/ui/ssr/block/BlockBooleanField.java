package cn.cerc.ui.ssr.block;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class BlockBooleanField extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String trueText = "是";
    @Column
    String falseText = "否";

    public BlockBooleanField(String title, String field) {
        super();
        this.title = title;
        this.field = field;
        init();
    }

    public BlockBooleanField() {
        super();
        init();
    }

    public void init() {
        block.display(1);
        block.option("_ratio", "1");
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format("""
                <div style='flex: ${_ratio};'>
                    <label>%s</label>
                    <span id='%s'>${if %s}%s${else}%s${endif}</span>
                </div>
                """, title, field, field, trueText, falseText));
        return block;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public SsrBlock block() {
        return block;
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

    @Override
    public String field() {
        return field;
    }

    @Override
    public ISupportBlock field(String field) {
        this.field = field;
        return this;
    }

}
