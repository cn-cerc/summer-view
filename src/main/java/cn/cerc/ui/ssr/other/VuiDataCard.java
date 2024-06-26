package cn.cerc.ui.ssr.other;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.IHandle;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.SsrFormStyleDefault;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.page.VuiCanvas;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("数据卡片")
@VuiCommonComponent
public class VuiDataCard extends VuiControl implements ISupportModule {
    private IHandle handle;

    @Column(name = "数据卡片代码")
    String code = "";
    @Column(name = "数据卡片名称")
    String title = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle(handle);
            break;
        }
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        EditorForm editForm = new EditorForm(content, this);
        VuiForm form = editForm.getForm();
        SsrFormStyleDefault style = form.defaultStyle();
        form.addBlock(style.getString("卡片代码", "code").readonly(true).dialog("showDataCardDialog", "title"));
        form.addBlock(style.getString("卡片名称", "title").readonly(true));
        form.dataRow().setValue("code", code).setValue("title", title);
        editForm.build();
    }

    @Override
    public void output(HtmlWriter html) {
        if (handle instanceof AbstractForm form) {
            VuiDataCardRuntime cardEnv = new VuiDataCardRuntime(form);
            cardEnv.setPageCode(String.format("%s.execute", code));
            html.print("<div class='dataCard'");
            var props = this.properties();
            if (props.has("v_style"))
                html.print(" style='%s'", props.get("v_style").asText());
            html.println(">");
            VuiCanvas canvas = cardEnv.getCanvas();
            canvas.output(html);
            html.print("</div>");
        }
    }

    public VuiDataCard handle(IHandle handle) {
        this.handle = handle;
        return this;
    }

}
