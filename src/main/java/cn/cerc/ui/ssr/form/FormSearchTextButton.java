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
    String placeholder = "请输入查询条件";
    @Column
    String field = "SearchText_";
    @Column
    boolean autofocus = false;
    @Column
    String maxRecordField = "";

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block = owner.addBlock(VuiForm.FormStart,
                String.format(
                        """
                                <div class="searchHead searchTextButton" ${if _style}style='${_style}'${endif}>
                                    <a role="configTemplate" class="hoverImageBox" type="button" onclick="showSsrConfigDialog('${templateId}')">
                                        <img src="%s" />
                                        <img src="%s" />
                                    </a>
                                    ${if _isPhone}
                                        ${if _maxRecordField is not empty}
                                        <li class="searchTextDiv searchMaxRecord">
                                            <label>载入笔数</label>
                                            <div>
                                                <input type="number" name="${_maxRecordField}" id="${_maxRecordField}" value="${%s}" autocomplete="off" placeholder="${if _isPhone}笔数${else}请输入载入笔数${endif}" onclick="this.select();">
                                                <span role="suffix-icon"></span>
                                            </div>
                                        </li>
                                        ${endif}
                                    ${endif}
                                    <li class="searchTextDiv">
                                        <label>查询条件</label>
                                        <div>
                                            <input type="text" name="${_field}" id="${_field}" value="${%s}" autocomplete="off" placeholder="${_placeholder}" ${if _autofocus}autofocus ${endif}>
                                            <span role="suffix-icon"></span>
                                        </div>
                                    </li>
                                    ${if not _isPhone}
                                        ${if _maxRecordField is not empty}
                                        <li class="searchTextDiv searchMaxRecord">
                                            <label>载入笔数</label>
                                            <div>
                                                <input type="number" name="${_maxRecordField}" id="${_maxRecordField}" value="${%s}" autocomplete="off" placeholder="${if _isPhone}笔数${else}请输入载入笔数${endif}" onclick="this.select();">
                                                <span role="suffix-icon"></span>
                                            </div>
                                        </li>
                                        ${endif}
                                    ${endif}
                                    <div class="searchFormButtonDiv">
                                        <button name="submit" value="search">查询</button>
                                    </div>
                                </div>
                                       """,
                        getImage("images/icon/templateConfig.png"), getImage("images/icon/templateConfig_hover.png"),
                        this.maxRecordField, field, this.maxRecordField));
        block.id(VuiForm.FormStart).fields(this.field, this.maxRecordField);

        boolean isPhone = false;
        if (owner instanceof VuiForm form) {
            isPhone = form.isPhone();
        }
        block.option("templateId", "")
                .option("_placeholder", this.placeholder)
                .option("_field", this.field)
                .option("_style", this.properties("v_style").orElse(""))
                .option("_autofocus", this.autofocus ? "1" : "")
                .option("_isPhone", isPhone ? "1" : "")
                .option("_maxRecordField", this.maxRecordField);
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

    public FormSearchTextButton autofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    public FormSearchTextButton maxRecord(String maxRecordField) {
        this.maxRecordField = maxRecordField;
        return this;
    }
}
