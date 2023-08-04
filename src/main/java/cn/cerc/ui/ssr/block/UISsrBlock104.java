package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

/**
 * 手机端UISsrChunk列表组件， 一行三列，每一列横向展示
 */
public class UISsrBlock104 extends UISsrBoard {

    public UISsrBlock104(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                <li role='UISsrBlock104'>
                    ${callback(slot0)}
                    ${callback(slot1)}
                    ${callback(slot2)}
                </li>
                """));
    }

    @Override
    public UISsrBoard slot0(SupplierBlockImpl slot) {
        UISsrBoard slot0 = super.slot0(slot);
        super.slot0().option("_hasColon", "1");
        return slot0;
    }

    @Override
    public UISsrBoard slot1(SupplierBlockImpl slot) {
        UISsrBoard slot1 = super.slot1(slot);
        super.slot1().option("_hasColon", "1");
        return slot1;
    }

    @Override
    public UISsrBoard slot2(SupplierBlockImpl slot) {
        UISsrBoard slot2 = super.slot2(slot);
        super.slot2().option("_hasColon", "1");
        return slot2;
    }

}
