package cn.cerc.ui.ssr.chart;

import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("折线图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChartLine extends VuiControl implements ICommonSupportChart, ISupportPanel, ISupplierBlock {
    private static final Logger log = LoggerFactory.getLogger(ChartBar.class);
    private SsrBlock block = new SsrBlock("");
    @Column
    String title = "";
    @Column
    String field = "";
    @Column(name = "是否以柱状图方式展示")
    boolean isBar = false;
    @Column
    Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    public ChartLine() {
        super();
    }

    @Override
    public void saveEditor(RequestReader reader) {
        super.saveEditor(reader);
        if (Utils.isEmpty(title))
            this.title = reader.getString("binder")
                    .map(serviceId -> canvas().getMember(serviceId, binder.targetType()).orElse(null))
                    .map(VuiDataService::serviceDesc)
                    .orElse(reader.getString("title").orElse(""));
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
            if (this.binder.target().isEmpty()) {
                log.warn("未设置数据源：dataSet");
                break;
            }
            Optional<VuiDataService> service = this.binder.target();
            if (service.isPresent()) {
                if (sender == service.get()) {
                    DataSet dataSet = service.get().dataSet();
                    if (dataSet.eof())
                        dataSet.append().setValue("key", "(无)").setValue("值", 0);
                    String title = dataSet.head().getString("title") + this.getClass().getSimpleName();
                    block.option("_data_title", title);
                    block.option("_data", dataSet.json());
                }
            } else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.binder.targetId());
            break;
        }
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        owner.addBlock(this.title, block.text("""
                <div role='chart' data-title='${_data_title}'></div>
                <script>$(function(){buildChartByDataSet(`${_data}`, '${_type}', '${_data_title}')})</script>
                """));
        block.option("_type", isBar ? "bar" : "line");
        block.id(title).display(1);
        return block;
    }

    @Override
    public String fields() {
        return this.fields();
    }

    @Override
    public ICommonSupportChart field(String field) {
        return this.field(field);
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ICommonSupportChart title(String title) {
        this.title = title;
        return this;
    }

}
