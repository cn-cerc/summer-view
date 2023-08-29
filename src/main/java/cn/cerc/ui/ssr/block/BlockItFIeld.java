package cn.cerc.ui.ssr.block;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockItFIeld extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();

    @Column
    String title = "序";

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
    public String getIdPrefix() {
        return "it";
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ISupportBlock title(String title) {
        this.title = title;
        return this;
    }

}
