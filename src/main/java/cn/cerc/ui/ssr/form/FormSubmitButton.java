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
@Description("查询头")
@VuiCommonComponent
public class FormSubmitButton extends VuiControl implements ISupplierBlock, ISupportForm {
    private ImageConfigImpl imageConfig;
    private SsrBlock block;
    @Column
    String title = "查询";
    @Column
    String field = "search";
    @Column
    boolean searchButton = true;

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block = owner.addBlock(VuiForm.FormStart, String.format("""
                    <div class='searchHead' ${if _style}style='${_style}'${endif}>
                        ${if _searchButton}
                        <span onclick="toggleSearch(this)">查询条件</span>
                        ${endif}
                        <div class="searchFormButtonDiv">
                            <button name="submit" value="${_field}">${_title}</button>
                            ${if templateId}
                            <a role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">
                                <img class="default" src="%s" />
                                <img class="hover" src="%s" />
                            </a>
                            ${endif}
                        </div>
                    </div>
                """, getImage("images/icon/templateConfig.png"), getImage("images/icon/templateConfig_hover.png")));
        block.option("templateId", "")
                .option("_field", this.field)
                .option("_title", this.title)
                .option("_searchButton", this.searchButton ? "1" : "0")
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
        return "submitButton";
    }

}
