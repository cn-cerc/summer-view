package cn.cerc.ui.ssr.excel;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsRow extends VuiContainer<ISupportXlsRow> implements ISupportXls {

    @Override
    public void output(HtmlWriter html) {
        super.output(html);
        this.canvas().sendMessage(this, SsrMessage.SheetNextRow, 1, null);
    }

}
