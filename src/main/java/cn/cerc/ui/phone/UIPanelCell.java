package cn.cerc.ui.phone;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.fields.UIStarFlag;
import cn.cerc.ui.grid.FieldStyleDefine;
import cn.cerc.ui.vcl.UIForm.UIFormGatherImpl;

public class UIPanelCell extends UIComponent implements UIFormGatherImpl {
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
            DataRow row = impl.currentRow().orElseThrow();
            String name = row.fields().get(fieldCode).name();
            if (!Utils.isEmpty(name)) {
                // 若有需要星标，则予以显示
                var style = impl.dataStyle();
                if (style != null) {
                    FieldStyleDefine field = style.fields().get(fieldCode);
                    if (field != null) {
                        if (field.required() && !field.readonly())
                            new UIStarFlag(null).output(html);
                    }
                }
                html.print(name);
                html.print(":");
            }
            html.print(row.getText(fieldCode));
        } else
            html.print("UIDataViewImpl is null");
        this.endOutput(html);
    }

    @Override
    public int gatherRequest(HttpServletRequest request) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl != null) {
            String value = request.getParameter(fieldCode);
            boolean readonly = impl.readonly();
            if (impl.dataStyle() != null)
                readonly = impl.dataStyle().fields().get(this.fieldCode).readonly();
            if (!readonly) {
                impl.currentRow().orElseThrow().setValue(this.fieldCode, value);
                return 1;
            }
        }
        return 0;
    }

}
