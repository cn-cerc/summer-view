package cn.cerc.ui.ssr.page;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.vcl.ext.UIBottom;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("页面底部按钮")
@VuiCommonComponent
public class VuiFooterButton extends VuiComponent implements ISupportCanvas {
    @Column
    String text = "button";
    @Column
    String href = "";
    @Column
    String target = "";
    @Column(name = "全选目标Id")
    String checkAllTargetId = "";
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(this, ISupplierDataRow.class);

    @Override
    public String getIdPrefix() {
        return "button";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitFooter:
            if (msgData instanceof IFooter footer) {
                if (!Utils.isEmpty(checkAllTargetId))
                    footer.setCheckAllTargetId(checkAllTargetId);
                UIBottom button = footer.addButton();
                button.setCaption(text);

                SsrBlock url = new SsrBlock(this.href);
                Optional<ISupplierDataRow> optDataRow = this.dataRow.target();
                if (optDataRow.isPresent())
                    url.dataRow(optDataRow.get().dataRow());

                button.setUrl(url.html());
                if (!Utils.isEmpty(target))
                    button.setTarget(target);
                button.setId(this.getId());
            }
            break;
        }
    }

}
