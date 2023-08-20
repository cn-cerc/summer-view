package cn.cerc.ui.ssr.base;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.AlignEnum;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.page.ISupportCanvas;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("通用面板")
public class VuiPanel extends VuiContainer<ISupportPanel> implements ISupportCanvas {
    SsrBlock block = new SsrBlock();
    @Column
    String v_type = "div";
    @Column
    String v_role = "";
    @Column
    String v_class = "";
    @Column
    AlignEnum align = AlignEnum.None;

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
    public void beginOutput(HtmlWriter html) {
        html.print("<");
        html.print(this.v_type);
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

}
