package cn.cerc.ui.ssr.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.AlignEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISsrTemplateConfig;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.page.VuiEnvironment;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("通用面板")
public class VuiPanel extends VuiContainer<ISupportPanel>
        implements ISsrBoard, ISupportPanel, ISupportCanvas, ISupportChart {
    SsrBlock block = new SsrBlock();
    private SsrTemplate template = new SsrTemplate("");
    private List<String> columns = new ArrayList<>();
    private IHandle handle;
    @Column
    String v_type = "div";
    @Column
    String v_role = "";
    @Column
    String v_class = "";
    @Column
    AlignEnum align = AlignEnum.None;
    @Column
    private boolean enableConfig = true;

    @Override
    public String getIdPrefix() {
        return "panel";
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorPanel panel = new EditorPanel(content, this);
        panel.build(pageCode);
        // 显示所有可以加入的组件
        EditorForm form2 = new EditorForm(content, this);
        form2.addClassList(ISupportPanel.class, VuiButton.class);
        form2.build();
    }

    @Override
    public void output(HtmlWriter html) {
        if (enableConfig) {
            this.beginOutput(html);
            var props = this.properties();
            if (props.has("v_inner_align") && "Grid".equals(props.get("v_inner_align").asText())) {
                var cols = props.get("v_column_num").asInt();
                html.print("<div role='grid'>");
                int count = columns.size() / cols * cols;
                if (columns.size() % cols > 0) {
                    count += cols;
                }
                for (int i = 0; i < count; i++) {
                    if (i % cols == 0) {
                        html.print("<div role='grid-row'>");
                    }
                    html.print(String.format("<div role='grid-col'%s>",
                            props.has("v_inner_style")
                                    ? String.format(" style='%s'", props.get("v_inner_style").asText())
                                    : ""));
                    if (i < columns.size()) {
                        this.getBlock(columns.get(i)).ifPresent(t -> html.print(t.html()));
                    }
                    html.print("</div>");
                    if (i % cols == cols - 1) {
                        html.print("</div>");
                    }
                }
                html.println("</div>");
            } else {
                super.output(html);
            }
            this.endOutput(html);
        } else {
            super.output(html);
        }
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        html.print("<");
        html.print(this.v_type);
        if (this.properties("v_style").isPresent())
            html.print(String.format(" style='%s'", this.properties("v_style").get()));
        if (!Utils.isEmpty(this.v_role))
            html.print(String.format(" role='%s'", this.v_role));
        if (!Utils.isEmpty(this.v_class))
            html.print(String.format(" class='%s'", this.v_class));
        html.print(">");
    }

    @Override
    public void endOutput(HtmlWriter html) {
        html.print(String.format("</%s>", this.v_type));
    }

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        for (var item : this) {
            if (item instanceof ISupplierBlock supplier)
                this.addBlock(supplier);
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitContent:
            if (enableConfig) {
                if (canvas().environment() instanceof VuiEnvironment environment) {
                    String pageCode = environment.getPageCode();
                    this.templateId(pageCode + "_panel");
                    this.columns.clear();
                    this.loadConfig(handle);
                }
            }
            break;
        }
    }

    public void loadConfig(IHandle handle) {
        var context = Application.getContext();
        var bean = context.getBean(ISsrTemplateConfig.class);
        DataSet defaultDataSet = this.getDefaultOptions();
        if (defaultDataSet != null && !defaultDataSet.eof())
            for (var field : bean.getFields(handle, defaultDataSet))
                this.addColumn(field);
    }

    /**
     * 请改使用 loadConfig
     */
    public DataSet getDefaultOptions() {
        DataSet ds = new DataSet();
        for (var ssr : template) {
            var option = ssr.option(ISsrOption.Display);
            String id = ssr.id();
            if (option.isPresent()) {
                if (id.startsWith("body.") || id.startsWith("head."))
                    id = id.substring(5, id.length());
                if (ds.locate("column_name_", id)) {
                    if (ssr.id().startsWith("body."))
                        ds.edit().setValue("column_name_", id).setValue("option_", option.get());
                } else
                    ds.append().setValue("column_name_", id).setValue("option_", option.get());
            }
        }
        ds.head().setValue("template_id_", template.id());
        return ds;
    }

    @Override
    public SsrTemplate template() {
        return template;
    }

    public VuiPanel templateId(String id) {
        template.id(id);
        return this;
    }

    public String templateId() {
        return template.id();
    }

    @Override
    public List<String> columns() {
        return columns;
    }

}
