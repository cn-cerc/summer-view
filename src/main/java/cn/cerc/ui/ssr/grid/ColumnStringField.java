package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ColumnStringField extends VuiControl implements ISupportGridColumn {
    private DataSet dataSet;
    @Column
    String field = "";

    @Override
    public void dataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!Utils.isEmpty(this.field))
            html.print(dataSet.current().getText(this.field));
    }

}
