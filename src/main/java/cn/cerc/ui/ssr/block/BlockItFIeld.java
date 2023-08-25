package cn.cerc.ui.ssr.block;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockItFIeld implements ISupportBlock {
    private SsrBlock block = new SsrBlock();

    public BlockItFIeld() {
        super();
        block.display(1);
    }

    @Override
    public SsrBlock request(ISsrBoard line) {
        block.text("""
                <div role='gridIt'>
                    <span>${dataset.rec}</span>
                </div>
                """);
        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

}
