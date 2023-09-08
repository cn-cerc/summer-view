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
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("数据仪表盘")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChartRing extends VuiControl implements ICommonSupportChart, ISupportPanel, ISupplierBlock {
    private static final Logger log = LoggerFactory.getLogger(ChartRing.class);
    private SsrBlock block = new SsrBlock("");
    private ImageConfigImpl imageConfig;

    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    public ChartRing() {
        super();
        init();
    }

    private void init() {
        block.option("_data", "");
        imageConfig = Application.getBean(ImageConfigImpl.class);
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
    public String fields() {
        return field;
    }

    @Override
    public ChartRing field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public ChartRing title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
        case SsrMessage.AfterSubmit:
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
                    if (!dataSet.eof())
                        block.option("_data", dataSet.json());
                }
            } else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.binder.targetId());
            break;
        }
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        owner.addBlock(this.title, block.text(String.format("""
                <div role='chart' data-title='${_data_title}'>${if not _data}<div role='noData'>
                    <img src='%s' />
                    <span>数据源为空或者未绑定数据源</span>
                </div>${endif}</div>
                <script>$(function(){buildRingChart(`${_data}`, '${_data_title}')})</script>
                """, imageConfig.getCommonFile("images/Frmshopping/notDataImg.png"))));
        block.id(title).display(1);
        return block;
    }

}
