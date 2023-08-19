package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISupplierBlock;
import cn.cerc.ui.ssr.UISsrBoard;

/**
 * 手机端UISsrChunk列表的内容组件，两个插槽，一行两列，标题和内容纵向展示，默认每列宽度占比为1:1，支持ratio方法设置比例
 * 0000000000000000000000000000000000000
 * 00       111          111          00
 * 00       22222        22222        00
 * 0000000000000000000000000000000000000
 */
public class UISsrBlock2201 extends UISsrBoard {

    public UISsrBlock2201(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='UISsrBlock2201'>
                        ${callback(slot0)}
                        ${callback(slot1)}
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

    public UISsrBlock2201 ratio(int slot0Ratio, int slot1Ratio) {
        super.slot0().option("_ratio", String.valueOf(slot0Ratio));
        super.slot1().option("_ratio", String.valueOf(slot1Ratio));
        return this;
    }
}
