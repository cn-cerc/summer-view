package cn.cerc.ui.ssr.source;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiComponent;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiListValue extends VuiComponent implements ISupplierList {
    @Column(name = "附加数据源")
    String target = "";
}
