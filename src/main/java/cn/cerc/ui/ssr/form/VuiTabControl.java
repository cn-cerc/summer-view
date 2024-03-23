package cn.cerc.ui.ssr.form;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Description("分组控制器")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiTabControl extends VuiControl implements ISupportForm, IBinders {

    private Binders binders = new Binders();

    @Override
    public Binders binders() {
        return this.binders;
    }

    @Override
    public void output(HtmlWriter html) {
        List<VuiComponent> components = binders.stream().map(Binder::owner).toList();
        StringBuilder head = new StringBuilder();
        StringBuilder body = new StringBuilder();
        int index = 0;
        for (VuiComponent component : components) {
            if (component instanceof VuiTabSheet sheet && !Utils.isEmpty(sheet.name)) {
                if (index == 0) {
                    head.append("<div class='tabHead'><nav>");
                    body.append("<div class='tabBody'>");
                }
                head.append(String.format("<span%s onClick='updateTabSheetIndex(this)' data-index='%s'>%s</span>",
                        index == 0 ? " class='selected'" : "", index, sheet.name));
                body.append(String.format("<ul%s>", index == 0 ? " class='selected'" : ""));
                for (UIComponent field : component.getComponents()) {
                    body.append(field.toString());
                }
                body.append("</ul>");
                index++;
            }
        }
        if (index > 0) {
            head.append("</nav></div>");
            body.append("</div>");
        }
        html.print(head.toString());
        html.print(body.toString());
    }

}
