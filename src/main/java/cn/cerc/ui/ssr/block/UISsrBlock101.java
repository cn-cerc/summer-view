package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

public class UISsrBlock101 extends UISsrBoard {

    public UISsrBlock101(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='title'>
                        <div>
                            <span role="gridIt">${dataset.rec}</span>
                        </div>
                        ${callback(slot0)}
                        <div role='titleRight'>${callback(slot1)}</div>
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

}
