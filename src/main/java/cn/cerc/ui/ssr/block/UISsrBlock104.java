package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

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
        return super.slot0(slot);
    }

    @Override
    public UISsrBoard slot1(SupplierBlockImpl slot) {
        return super.slot1(slot);
    }

    @Override
    public UISsrBoard slot2(SupplierBlockImpl slot) {
        return super.slot2(slot);
    }

}
