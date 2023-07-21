package cn.cerc.ui.style;

import java.util.Optional;

import cn.cerc.db.core.DataSet;

public interface SsrComponentImpl {

    void addField(String... field);

    DataSet getDefaultOptions();

    void setConfig(DataSet configs);

    Object addTemplate(String id, String templateText);

    Optional<SsrTemplateImpl> getTemplate(String templateId);

}
