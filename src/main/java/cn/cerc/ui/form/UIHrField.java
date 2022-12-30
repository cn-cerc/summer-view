package cn.cerc.ui.form;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/**
 * 装饰Field基类
 */
public class UIHrField extends UIAbstractField {

    public UIHrField(UIComponent owner) {
        super(owner);
        this.setLineWidth(12);
    }
    
    @Override
    public void writeContent(HtmlWriter html) {
        html.print("<hr />");
    }

}
