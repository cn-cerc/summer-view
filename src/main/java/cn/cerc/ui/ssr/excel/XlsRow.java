package cn.cerc.ui.ssr.excel;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiContainer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsRow extends VuiContainer<ISupportXlsRow> implements ISupportXls {

}
