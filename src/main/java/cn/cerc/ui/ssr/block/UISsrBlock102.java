package cn.cerc.ui.ssr.block;

import java.util.function.Supplier;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.SupplierBlockImpl;
import cn.cerc.ui.ssr.UISsrBoard;

/**
 * 手机端UISsrChunk列表组件，带多选框的头部标题区域
 */
public class UISsrBlock102 extends UISsrBoard {

    public UISsrBlock102(UIComponent owner, String checkboxField, String checkboxValueField) {
        super(owner);
        this.cpu(new SsrBlock(String.format("""
                        <li role='title'>
                        <div>
                            <input type="checkbox" name="%s" value="${%s}"/>
                            <span role="gridIt">${dataset.rec}</span>
                        </div>
                        ${callback(slot0)}
                        ${callback(slot1)}
                    </li>
                """, checkboxField, checkboxValueField)));
    }

    public UISsrBlock102(UIComponent owner, String checkboxField, Supplier<String> checkboxValue) {
        super(owner);
        this.cpu(new SsrBlock(String.format("""
                        <li role='title'>
                        <div>
                            <input type="checkbox" name="%s" value="${callback(checkboxValue)}"/>
                            <span role="gridIt">${dataset.rec}</span>
                        </div>
                        ${callback(slot0)}
                        ${callback(slot1)}
                    </li>
                """, checkboxField)).onCallback("checkboxValue", checkboxValue));
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
