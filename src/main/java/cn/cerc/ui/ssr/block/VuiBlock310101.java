package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;

/**
 * 手机端UISsrChunk列表的标题组件，三个插槽，一行三列，序号、标题和内容横向展示
 * 0000000000000000000000000000000000000
 * 00  111    111 2222222        111  00
 * 0000000000000000000000000000000000000
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("手机表格行310101")
@VuiCommonComponent
public class VuiBlock310101 extends VuiBoard implements ISupportBoard {

    public VuiBlock310101() {
        this(null);
    }
    
    public VuiBlock310101(UIComponent owner) {
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
    public VuiBoard slot0(ISupplierBlock slot) {
        return super.slot0(slot);
    }

    @Override
    public VuiBoard slot1(ISupplierBlock slot) {
        return super.slot1(slot);
    }

    @Override
    public VuiBoard slot2(ISupplierBlock slot) {
        return super.slot2(slot);
    }

}
