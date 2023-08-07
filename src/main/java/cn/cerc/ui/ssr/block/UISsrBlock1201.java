package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

/**
 * 手机端UISsrChunk列表的内容组件，一个插槽，一行一列，标题和内容纵向展示
 */
public class UISsrBlock1201 extends UISsrBoard {

    public UISsrBlock1201(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='UISsrBlock1201'>
                        ${callback(slot0)}
                    </li>
                """));
    }

    @Override
    public UISsrBoard slot0(SupplierBlockImpl slot) {
        return super.slot0(slot);
    }

}
