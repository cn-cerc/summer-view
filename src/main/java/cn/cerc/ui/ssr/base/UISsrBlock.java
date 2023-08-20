package cn.cerc.ui.ssr.base;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.SsrBlock;

public class UISsrBlock extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UISsrBlock.class);
    private SsrBlock block;

    public UISsrBlock() {
        super(null);
    }

    public UISsrBlock(UIComponent owner) {
        super(owner);
    }

    public UISsrBlock(UIComponent owner, String templateText) {
        super(owner);
        this.block = new SsrBlock(templateText);
    }

    public UISsrBlock(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        this.block = new SsrBlock(class1, id);
    }

    /**
     * 不允许在此对象下再增加子对象
     */
    @Override
    public UIComponent addComponent(UIComponent child) {
        throw new RuntimeException("the is not container");
    }

    @Override
    public void output(HtmlWriter html) {
        if (block != null)
            html.print(block.html());
        else
            log.warn("block is null");
    }

    public SsrBlock block() {
        Objects.requireNonNull(block);
        return block;
    }

    public UISsrBlock block(SsrBlock block) {
        this.block = block;
        return this;
    }

}
