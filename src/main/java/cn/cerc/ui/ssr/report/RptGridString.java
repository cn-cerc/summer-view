package cn.cerc.ui.ssr.report;

import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Paragraph;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptGridString extends AbstractRptGridControl {
    private Map<String, String> map;

    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);
    @Column
    String summaryValue = "";
    @Column
    SummaryTypeEnum summaryType = SummaryTypeEnum.无;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitBinder:
            mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = mapSource.target();
            if (optMap.isPresent())
                this.map = optMap.get().items();
            break;
        }
    }

    @Override
    public Paragraph outputTotal(DataSet dataSet) {
        String value = switch (summaryType) {
        case 计数 -> String.valueOf(dataSet.size());
        case 固定 -> summaryValue;
        default -> "";
        };
        return new Paragraph(value, RptFontLibrary.f10());
    }

    @Override
    public SummaryTypeEnum summaryType() {
        return summaryType;
    }

    @Override
    protected String content() {
        if (dataSet != null) {
            String value = dataSet.getString(field);
            if (map != null)
                value = map.get(value);
            return value;
        }
        return "";
    }

}
