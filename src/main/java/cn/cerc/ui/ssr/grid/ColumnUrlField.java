package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ColumnUrlField extends SsrControl implements ISupportGridColumn {
    private DataSet dataSet;
    @Column(name = "描述文字")
    String text = "内容";
    @Column
    String href = "";
    @Column(name = "取值字段")
    String field = "";

    @Override
    public void dataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!Utils.isEmpty(this.href)) {
            SsrBlock ssr = new SsrBlock(String.format("<a href='%s'>${if _field}${%s}${else}%s${endif}</a>",
                    this.href, this.field, this.text));
            ssr.dataSet(dataSet);
            ssr.option("_field", this.field);
            html.println(ssr.html());
        }
    }

}
