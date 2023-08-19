package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.SsrBlock;

public class BlockField extends AbstractField {
    private SsrBlock block;

    public BlockField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public BlockField setBlock(SsrBlock block) {
        this.block = block;
        return this;
    }

    public SsrBlock getBlock() {
        return block;
    }

    @Override
    public String getText() {
        if (block != null) {
            block.dataSet(this.getDataSet().orElse(null));
            return block.html();
        } else
            return "template is null";
    }

}
