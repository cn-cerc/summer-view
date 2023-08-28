package cn.cerc.ui.ssr.block;

import javax.persistence.Column;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockBooleanField implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title;
    @Column
    String field;
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
                    <label for='%s'>%s</label>
                    <span id='%s'>${if %s}%s${else}%s${endif}</span>
                </div>
                """, field, title, field, field, trueText, falseText));
        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

}
