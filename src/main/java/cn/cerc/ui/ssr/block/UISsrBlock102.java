package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

public class UISsrBlock102 extends UISsrBoard {
    private String checkboxField = "checkBoxName";

    public UISsrBlock102(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock(String.format("""
                        <li role='title'>
                        <div>
                            <input type="checkbox" name="%s" value="1" ${if %s}checked ${endif}/>
                            <span role="gridIt">${dataset.rec}</span>
                        </div>
                        ${callback(slot0)}
                        ${callback(slot1)}
                    </li>
                """, checkboxField, checkboxField)).strict(false));
    }

    @Override
    public UISsrBoard slot0(SupplierBlockImpl slot) {
        return super.slot0(slot);
    }

    @Override
    public UISsrBoard slot1(SupplierBlockImpl slot) {
        return super.slot1(slot);
    }

    public void checkboxField(String checkboxField) {
        this.checkboxField = checkboxField;
    }

}
