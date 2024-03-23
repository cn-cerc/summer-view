package cn.cerc.ui.ssr.form;

import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("分组查询头")
@VuiCommonComponent
public class FormTabsHead extends VuiControl implements ISupportForm {
    private ImageConfigImpl imageConfig;
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);
    @Column(name = "选中页签")
    String selectIndex = "0";
    private SsrBlock block = new SsrBlock();

    @Override
    public void output(HtmlWriter html) {
        block.text(String.format(
                """
                        <div class='searchHead'>
                            <nav>${map.begin}<a href='${map.key}'${if map.key=='%s'} class='checked'${endif}>${map.value}</a>${map.end}</nav>
                            <div class='searchFormButtonDiv'>
                                <button name='submit' value='search'>查询</button>
                                ${if templateId}
                                <a role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">
                                    <img class="default" src="%s" />
                                    <img class="hover" src="%s" />
                                </a>
                                ${endif}
                            </div>
                        </div>
                        """,
                selectIndex, getImage("images/icon/templateConfig.png"),
                getImage("images/icon/templateConfig_hover.png")));
        String templateId = "";
        VuiForm form = this.findOwner(VuiForm.class);
        if (form != null && !Utils.isEmpty(form.templateId()))
            templateId = form.templateId();
        block.option(ISsrOption.TemplateId, templateId);
        block.toMap(Map.of());
        Optional<ISupplierMap> optMap = this.mapSource.target();
        if (optMap.isPresent()) {
            Map<String, String> map = optMap.get().items();
            block.toMap(map);
        }
        html.print(block.html());
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.mapSource.init();
            break;
        }
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        EditorForm form = new EditorForm(content, this);
        form.addProperties(this);
        form.build();
        mapSource.target().ifPresent(map -> {
            if (map instanceof VuiComponent vui)
                vui.onMessage(this, SsrMessage.InitProperties, null, null);
            VuiForm vuiForm = form.getForm();
            SsrFormStyleDefault defaultStyle = vuiForm.defaultStyle();
            vuiForm.addBlock(defaultStyle.getString("选中页签", "selectIndex").toMap(map.items()));
        });
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null) {
            ApplicationContext context = Application.getContext();
            if (context != null)
                imageConfig = context.getBean(ImageConfigImpl.class);
        }
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

}
