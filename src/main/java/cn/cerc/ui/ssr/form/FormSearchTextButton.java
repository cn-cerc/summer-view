package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("带模糊查询和配置开窗的查询头")
@VuiCommonComponent
public class FormSearchTextButton extends VuiControl implements ISupplierBlock, ISupportForm {
    private ImageConfigImpl imageConfig;
    private SsrBlock block;
    @Column
    String placeholder = "查询条件";
    @Column
    String field = "SearchText_";

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block = owner.addBlock(VuiForm.FormStart, String.format(
                """
                        <div class="searchHead searchTextButton" ${if _style}style='${_style}'${endif}>
                            <a role="configTemplate" class="hoverImageBox" type="button" onclick="showSsrConfigDialog('${templateId}')">
                                <img src="%s" />
                                <img src="%s" />
                            </a>
                            <li class="searchTextDiv">
                                <input type="text" name="${_field}" id="${_field}" value="${%s}" autocomplete="off" placeholder="请输入${_placeholder}">
                                <span role="suffix-icon"></span>
                            </li>
                            <div class="searchFormButtonDiv">
                                <button name="submit" value="search">查询</button>
                            </div>
                        </div>
                               """,
                getImage("images/icon/templateConfig.png"), getImage("images/icon/templateConfig_hover.png"), field));
        block.id(VuiForm.FormStart).fields(this.field);
        block.option("templateId", "")
                .option("_placeholder", this.placeholder)
                .option("_field", this.field)
                .option("_style", this.properties("v_style").orElse(""));
        return block;
    }

    @Override
    public void output(HtmlWriter html) {
        if (block != null)
            html.print(block.html());
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            var form = this.findOwner(VuiForm.class);
            if (form != null)
                this.request(form);
            break;
        }
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null) {
            ApplicationContext context = Application.getContext();
            if (context != null)
                imageConfig = context.getBean(ImageConfigImpl.class);
        }
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public String getIdPrefix() {
        return "searchButton";
    }

    public FormSearchTextButton title(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormSearchTextButton field(String field) {
        this.field = field;
        return this;
    }

}
