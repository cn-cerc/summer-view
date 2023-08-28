package cn.cerc.ui.ssr.block;

import java.util.function.Supplier;

import javax.persistence.Column;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockOperaField implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    Supplier<String> callBackUrl;
    @Column
    String field;

    public BlockOperaField(String field) {
        super();
        this.field = field;
        init();
    }

    public BlockOperaField(Supplier<String> callBackUrl) {
        super();
        this.callBackUrl = callBackUrl;
        init();
    }

    public BlockOperaField() {
        super();
        init();
    }

    public void init() {
        block.display(1);
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format("""
                    <div role='opera'>
                    <a ${if _callBackUrl}href='${callback(callBackUrl)}'${else}href='${%s}'${endif}>内容</a>
                </div>
                """, field));
        block.option("_callBackUrl", callBackUrl != null ? "1" : "");
        if (callBackUrl != null)
            block.onCallback("callBackUrl", callBackUrl);
        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

}
