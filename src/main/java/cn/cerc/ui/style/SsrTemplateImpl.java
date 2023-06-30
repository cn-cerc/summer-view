package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public interface SsrTemplateImpl {

    public String[] getParams();

    public DataRow getDataRow();

    public List<String> getList();

    public Map<String, String> getMap();

    public DataSet getDataSet();

}
