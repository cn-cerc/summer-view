package cn.cerc.ui.phone;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.fields.UIStarFlag;
import cn.cerc.ui.grid.FieldStyleData;

public class UIPanelCell extends UIComponent {
    private String fieldCode;

    public UIPanelCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    public UIPanelCell setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl != null)
            this.setCssProperty("data-field", this.fieldCode);
        this.beginOutput(html);
        if (impl != null) {
            String name = impl.dataSet().fields().get(fieldCode).name();
            if (!Utils.isEmpty(name)) {
                // 若有需要星标，则予以显示
                var style = impl.dataStyle();
                if (style != null) {
                    FieldStyleData field = style.fields().get(fieldCode);
                    if (field != null) {
                        if (field.starFlag())
                            new UIStarFlag(null).output(html);
                    }
                }
                html.print(name);
                html.print(":");
            }
            html.print(impl.dataSet().current().getText(fieldCode));
        } else
            html.print("UIDataViewImpl is null");
        this.endOutput(html);
    }

}
