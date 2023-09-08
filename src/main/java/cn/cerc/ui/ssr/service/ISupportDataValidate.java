package cn.cerc.ui.ssr.service;

import cn.cerc.mis.core.DataValidateException;

public interface ISupportDataValidate extends ISupportServiceHandler {

    void validate() throws DataValidateException;

}
