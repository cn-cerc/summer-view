package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlockImpl;

public class BlockField extends AbstractField {
    private SsrBlockImpl block;

    public BlockField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public BlockField setBlock(SsrBlockImpl block) {
        this.block = block;
        return this;
    }

    public SsrBlockImpl getBlock() {
        return block;
    }

    @Override
    public String getText() {
        if (block != null) {
            block.setDataSet(this.getDataSet().orElse(null));
            return block.getHtml();
        } else
            return "template is null";
    }

}
