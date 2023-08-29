package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

/**
 * 手机端UISsrChunk列表的内容组件，一个插槽，一行一列，标题和内容纵向展示
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("手机表格行1201")
public class VuiBlock1201 extends VuiBoard implements ISupportBoard {

    public VuiBlock1201() {
        this(null);
    }
    
    public VuiBlock1201(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                    <li role='UISsrBlock1201'>
                        ${callback(slot0)}
                    </li>
                """));
    }

    @Override
    public VuiBoard slot0(ISupplierBlock slot) {
        return super.slot0(slot);
    }

}
