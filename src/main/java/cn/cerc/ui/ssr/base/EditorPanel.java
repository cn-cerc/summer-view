package cn.cerc.ui.ssr.base;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.grid.UISsrGrid;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIDiv;

public class EditorPanel extends UIComponent {

    private UISsrGrid grid;
    private UIComponent sender;
    private ImageConfigImpl imageConfig;

    public EditorPanel(UIComponent owner, VuiComponent sender) {
        super(owner);
        this.sender = sender;
        grid = new UISsrGrid(owner);
        grid.dataSet(new DataSet());
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    public void addColumn(String title, String field, int width) {
        var style = grid.defaultStyle();
        grid.addBlock(style.getString(title, field, width));
        grid.addColumn(title);
    }

    public DataRow addRow() {
        return grid.dataSet().append().current();
    }

    public void build(String pageCode) {
        for (var item : sender) {
            if (item instanceof VuiComponent)
                this.addRow().setValue("id", item.getId()).setValue("class", item.getClass().getSimpleName());
        }

        String dragTitle = "head.drag";
        String dragBody = "body.drag";
        var ssrdrag1 = grid.addBlock(dragTitle, String.format("<th style='width: 5em'></th>"));
        ssrdrag1.id(dragTitle);
        var ssrdrag2 = grid.addBlock(dragBody, """
                <td draggable="true">
                    <img src="${img}" />
                </td>
                """);
        ssrdrag2.option("img", this.getImage("images/property.png"));
        ssrdrag2.id(dragBody);
        grid.addColumn("drag");

        this.addColumn("栏位", "id", 20);
        this.addColumn("类名", "class", 30);
        if (grid.dataSet().size() > 0) {
            var cid = sender.getId();
            String headTitle = "head.操作";
            String bodyTitle = "body.操作";
            var ssr1 = grid.addBlock(headTitle, String.format("<th style='width: 10em'>操作</th>"));
            ssr1.id(headTitle);
            var ssr2 = grid.addBlock(bodyTitle,
                    """
                            <td data-id="${callback(dataId)}">
                                <a href='javascript: updateLowCode("${pageCode}?mode=design", {id: "${cid}",removeComponent: "${callback(dataId)}",submit: true})'>
                                    删除
                                </a>
                            </td>
                            """);
            ssr2.onCallback("dataId", () -> grid.dataSet().getString("id"));
            ssr2.option("pageCode", pageCode);
            ssr2.option("cid", cid);
            ssr2.id(bodyTitle);
            grid.addColumn("操作");
        }
        UIDiv save = new UIDiv(this.getOwner());
        save.setCssProperty("lowcode", "button");
        UIButton button = new UIButton(save);
        button.setValue("save");
        button.setName("save");
        button.setOnclick(String.format("updateGrid(this, '%s?mode=design', '%s')", pageCode, sender.getId()));
        button.setText("保存");
    }

}
