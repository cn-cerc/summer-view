package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISupplierBlock;
import cn.cerc.ui.ssr.UISsrBoard;

/**
 * 手机端UISsrChunk列表的标题组件，三个插槽，一行三列，序号、标题和内容横向展示
 * 0000000000000000000000000000000000000
 * 00  111    111 2222222        111  00
 * 0000000000000000000000000000000000000
 */
public class UISsrBlock310101 extends UISsrBoard {

    public UISsrBlock310101(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='UISsrBlock310101'>
                        ${callback(slot0)}
                        ${callback(slot1)}
                        ${callback(slot2)}
                    </li>
                """));
    }

    @Override
    public UISsrBoard slot0(ISupplierBlock slot) {
        return super.slot0(slot);
    }

    @Override
    public UISsrBoard slot1(ISupplierBlock slot) {
        return super.slot1(slot);
    }

    @Override
    public UISsrBoard slot2(ISupplierBlock slot) {
        return super.slot2(slot);
    }

}
