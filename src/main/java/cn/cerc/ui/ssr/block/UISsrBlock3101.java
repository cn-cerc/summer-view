package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

/**
 * 手机端UISsrChunk列表的内容组件，三个插槽，一行三列，标题和内容横向展示，默认每列宽度占比为1:1:1，支持ratio方法设置比例
 * 0000000000000000000000000000000000000
 * 00   111 222   111 222   111 222   00
 * 0000000000000000000000000000000000000
 */
public class UISsrBlock3101 extends UISsrBoard {

    public UISsrBlock3101(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='UISsrBlock3101'>
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

    public UISsrBlock3101 ratio(int slot0Ratio, int slot1Ratio, int slot2Ratio) {
        super.slot0().option("_ratio", String.valueOf(slot0Ratio));
        super.slot1().option("_ratio", String.valueOf(slot1Ratio));
        super.slot2().option("_ratio", String.valueOf(slot2Ratio));
        return this;
    }
}
