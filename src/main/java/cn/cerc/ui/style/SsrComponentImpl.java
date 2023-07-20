package cn.cerc.ui.style;

import cn.cerc.db.core.DataSet;

public interface SsrComponentImpl {

    void addField(String... field);

    DataSet getDefaultOptions();
}
