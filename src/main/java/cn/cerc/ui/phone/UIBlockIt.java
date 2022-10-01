package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIBlockIt extends UIComponent {

    public UIBlockIt() {
        this(null);
    }

    public UIBlockIt(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        DataSource dataSource = dataSource();
        if (dataSource != null)
            html.print(dataSource.current().dataSet().recNo() + "#");
        this.endOutput(html);
    }

    private DataSource dataSource() {
        DataSource result = null;
        UIComponent parent = this.getOwner();
        while (parent != null) {
            if (parent instanceof DataSource) {
                result = (DataSource) parent;
                break;
            }
            parent = parent.getOwner();
        }
        return result;
    }

}
