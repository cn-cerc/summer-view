package cn.cerc.ui.form;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;

public class UIFormStyle implements UIFormStyleImpl {
    private DataRow dataRow = new DataRow();
    public HashMap<String, FormStockStyle> items = new LinkedHashMap<>();
    private String currentCaption;

    public UIFormStyle() {
        super();
    }

    public FormStockStyle formStock() {
        return items.get(currentCaption);
    }

    @Override
    public FormStockStyle addStock(String caption) {
        FormStockStyle formStock = new FormStockStyle(caption, dataRow);
        items.put(caption, formStock);
        currentCaption = caption;
        return formStock;
    }

    public FormLineStyleImpl addLine() {
        if (Utils.isEmpty(currentCaption)) {
            currentCaption = "表单信息";
            items.put(currentCaption, new FormStockStyle(currentCaption, dataRow));
        }
        return formStock().addLine(FormLineCol1Style.class);
    }

    @Override
    public DataRow dataRow() {
        return getDataRow();
    }

    public DataRow getDataRow() {
        return dataRow;
    }

    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

}
