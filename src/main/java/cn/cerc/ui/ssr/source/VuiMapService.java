package cn.cerc.ui.ssr.source;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.client.ServiceSign;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.report.ISupportRpt;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("Map数据服务")
@VuiCommonComponent
public class VuiMapService extends VuiComponent
        implements ISupplierMap, ISupportCanvas, ISupportXls, ISupportChart, ISupportRpt, IBinders {
    private Map<String, String> items = new LinkedHashMap<>();
    private String selected = "";
    private IHandle handle;
    private Binders binders = new Binders();

    @Column(name = "加入全部选择项")
    boolean addAll = false;
    @Column
    String service = "";
    @Column
    boolean remoteService = false;
    @Column
    String key = "";
    @Column
    String value = "";

    @Override
    public String getIdPrefix() {
        return "mapSource";
    }

    @Override
    public Map<String, String> items() {
        return items;
    }

    @Override
    public Optional<String> selected() {
        if (Utils.isEmpty(this.selected))
            return Optional.empty();
        return Optional.ofNullable(this.selected);
    }

    public VuiMapService selected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitProperties:
            if (!Utils.isEmpty(this.service) && !Utils.isEmpty(this.key) && !Utils.isEmpty(this.value)) {
                if (this.addAll)
                    items.put("", "全部");
//                if (this.remoteService)
//                    dataOut = new ServiceSign(this.service).callLocal(new CenterToken(handle)).dataOut();
//                else
                DataSet dataOut = new ServiceSign(this.service).callLocal(handle).dataOut();
                for (var row : dataOut)
                    items.put(row.getString(this.key), row.getString(this.value));
                if (this.addAll || dataOut.size() > 0)
                    binders.sendMessage(this, SsrMessage.InitMapSourceDone, null, null);
            }
            break;
        }
    }

    @Override
    public Binders binders() {
        return this.binders;
    }

}
