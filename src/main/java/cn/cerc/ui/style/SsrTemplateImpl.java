package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public interface SsrTemplateImpl {

    DataRow getDataRow();

    List<String> getList();

    Map<String, String> getMap();

    DataSet getDataSet();

    boolean isStrict();
}
