package cn.cerc.ui.ssr.other;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.VuiCanvas;
import cn.cerc.ui.ssr.page.VuiEnvironment;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiDataCardRuntime extends VuiEnvironment {

    private IPage page;

    private String templateId;

    public VuiDataCardRuntime(AbstractForm form) {
        super();
        this.form(form);

    }

    public VuiDataCardRuntime(AbstractForm form, IPage page) {
        super();
        this.form(form);
        this.page(page);
    }

    /**
     * 运行状态
     */
    public VuiCanvas getCanvas() {
        // 初始化环境变量
        VuiCanvas canvas = new VuiCanvas(this);
        canvas.sendMessage(canvas, SsrMessage.InitRequest, form.getRequest(), null);
        canvas.sendMessage(canvas, SsrMessage.InitHandle, form, null);
        canvas.sendMessage(canvas, SsrMessage.InitPage, page, null);
        canvas.sendMessage(canvas, SsrMessage.InitBinder, null, null);
        canvas.ready();
        return canvas;
    }

    /**
     * 新建时默认范例
     */
    @Override
    public String getSampleData(String pageCode) {
        // 先读取项目的基本配置
        String json = this.loadFile(VuiDataCardRuntime.class, pageCode);
        if (!Utils.isEmpty(json))
            return json;

        var block = new SsrBlock("""
                {
                    "title": "数据卡片",
                    "readme": "（页面描述）",
                    "class": "VuiCanvas",
                    "id": "${pageCode}",
                    "container": "true",
                    "visual": "true",
                    "components": [
                        {
                            "class": "VuiDataService",
                            "id": "service1",
                            "container": "false",
                            "visual": "false",
                            "headIn": "dataRow1",
                            "service": "",
                            "callByInit": true
                        }
                    ]
                }""".trim());
        return block.toMap("pageCode", pageCode).strict(false).html();
    }

    @Override
    public Class<?> getSupportClass() {
        return ISupportChart.class;
    }

    @Override
    protected String table() {
        return "data-card";
    }

    @Override
    protected IPage getRuntimePage() {
        return null;
    }

    @Override
    protected IPage getDesignPage() {
        return null;
    }

    @Override
    protected String corpNo() {
        return "000000";
    }

    public VuiDataCardRuntime templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public String templateId() {
        if (Utils.isEmpty(templateId))
            return getPageCode() + "_dataCard"; // 默认值：数据卡片代码_dataCard
        return this.templateId;
    }

    public IPage page() {
        return page;
    }

    public void page(IPage page) {
        this.page = page;
    }

}
