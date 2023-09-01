package cn.cerc.ui.ssr.editor;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.excel.ISupportXlsGrid;
import cn.cerc.ui.ssr.form.ISupportForm;
import cn.cerc.ui.ssr.grid.ISupportGrid;
import cn.cerc.ui.ssr.grid.VuiGrid;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIDiv;

public class EditorGrid extends UIComponent {

    private VuiGrid grid;
    private VuiComponent sender;
    private ImageConfigImpl imageConfig;
    private String storage;

    public EditorGrid(UIComponent owner, VuiComponent sender) {
        super(owner);
        this.sender = sender;
        grid = new VuiGrid(owner);
        grid.dataSet(new DataSet());

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
            if (item instanceof ISupplierBlock) {
                String cloumn = item.getId();
                if (item instanceof ISupportForm formBlock && !Utils.isEmpty(formBlock.title()))
                    cloumn = formBlock.title();
                else if (item instanceof ISupportGrid gridBlock && !Utils.isEmpty(gridBlock.title()))
                    cloumn = gridBlock.title();
                this.addRow()
                        .setValue("id", item.getId())
                        .setValue("cloumn", cloumn)
                        .setValue("class", item.getClass().getSimpleName());
            } else if (item instanceof ISupportXlsGrid gridBlock && !Utils.isEmpty(gridBlock.title())) {
                String cloumn = gridBlock.title();
                this.addRow()
                        .setValue("id", item.getId())
                        .setValue("cloumn", cloumn)
                        .setValue("class", item.getClass().getSimpleName());
            }
        }

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
                                <a href='javascript: updateLowCode("${pageCode}?mode=design&storage=${storage}", {id: "${cid}",removeComponent: "${callback(dataId)}",submit: true})'>
                                    删除
                                </a>
                            </td>
                            """);
            ssr2.onCallback("dataId", () -> grid.dataSet().getString("id"));
            ssr2.option("pageCode", pageCode);
            ssr2.option("storage", sender.canvas().storage());
            ssr2.option("cid", cid);
            ssr2.id(bodyTitle);
            grid.addColumn("操作");
        }
        UIDiv save = new UIDiv(this.getOwner());
        save.setCssProperty("lowcode", "button");
        UIButton button = new UIButton(save);
        button.setValue("save");
        button.setName("save");
        button.setOnclick(String.format("updateGrid(this, '%s?mode=design&storage=%s', '%s')", pageCode,
                sender.canvas().storage(), sender.getId()));
        button.setText("保存");
    }

    public String getStorage() {
        return storage;
    }

    public EditorGrid setStorage(String storage) {
        this.storage = storage;
        return this;
    }

}
