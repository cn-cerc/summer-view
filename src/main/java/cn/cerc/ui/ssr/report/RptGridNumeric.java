package cn.cerc.ui.ssr.report;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Paragraph;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptGridNumeric extends AbstractRptGridControl {
    private List<String> list;

    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String format = "";
    @Column
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);
    @Column
    SummaryTypeEnum summaryType = SummaryTypeEnum.无;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitBinder:
            listSource.init();
            break;
        case SsrMessage.InitListSourceDone:
            Optional<ISupplierList> optList = listSource.target();
            if (optList.isPresent())
                this.list = optList.get().items();
            break;
        }
    }

    @Override
    public Paragraph outputTotal(DataSet dataSet) {
        if (summaryType == SummaryTypeEnum.无)
            return new Paragraph();
        DoubleStream doubleStream = dataSet.records().stream().mapToDouble(row -> row.getDouble(field));
        double summary = switch (summaryType) {
        case 求和 -> doubleStream.sum();
        case 最大 -> doubleStream.max().orElse(0d);
        case 最小 -> doubleStream.min().orElse(0d);
        case 平均 -> doubleStream.average().orElse(0d);
        case 计数 -> doubleStream.count();
        default -> 0d;
        };
        return Utils.isEmpty(format) ? new Paragraph(String.valueOf(summary), RptFontLibrary.f10())
                : new Paragraph(new DecimalFormat(format).format(summary), RptFontLibrary.f10());
    }

    @Override
    public SummaryTypeEnum summaryType() {
        return summaryType;
    }

    @Override
    protected String content() {
        if (dataSet != null) {
            String value = dataSet.getString(field);
            if (list != null)
                value = list.get(dataSet.getInt(field));
            return value;
        }
        return "";
    }

}
