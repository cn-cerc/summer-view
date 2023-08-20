package cn.cerc.ui.ssr.other;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.VuiComponent;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiExportExcel extends VuiComponent implements ISupportPanel {

}
