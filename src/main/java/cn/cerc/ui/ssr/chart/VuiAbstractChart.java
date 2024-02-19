package cn.cerc.ui.ssr.chart;

import java.util.Objects;

import javax.persistence.Column;

import cn.cerc.ui.core.ViewDisplay;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.other.VuiDataCardRuntime;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

public abstract class VuiAbstractChart extends VuiControl
        implements ICommonSupportChart, ISupportPanel, ISupplierBlock {

    protected ImageConfigImpl imageConfig;

    @Column
    protected String title = "";

    @Column(name = "宽度占比")
    protected int width = 1;

    @Column(name = "高度占比")
    protected int height = 1;

    @Column(name = "用户自定义显示")
    protected ViewDisplay display_option = ViewDisplay.选择显示;

    @Column
    protected Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    @Override
    public String title() {
        return title;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        SsrBlock block = Objects.requireNonNull(block());
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            this.binder.target().ifPresent(item -> item.callByInit(false));// 让DataService不要在初始化时调用服务
            this.request(null);
            var board = this.findOwner(ISsrBoard.class);
            if (board != null) {
                board.addBlock(title, block);
            }
            break;
        case SsrMessage.InitContent:
            IVuiEnvironment env = canvas().environment();
            if (env instanceof VuiDataCardRuntime runtime) {
                block.option("_show_eye", "0");
                block.option("_templateId", runtime.templateId());
            } else if (env instanceof VuiEnvironment environment) {
                String templateId = environment.getPageCode() + "_panel";
                block.id(templateId);
                block.option("_templateId", templateId);
            }
            this.binder.target().ifPresent(item -> {
                block.option("_service", item.service());
                block.option("_dataIn", item.dataIn().json());
                String title = item.serviceDesc();
                block.option("_data_title", title + this.getClass().getSimpleName());
                block.option("_title", title);
            });
            break;
        }
    }

    protected SsrBlock block() {
        return null;
    }
}
