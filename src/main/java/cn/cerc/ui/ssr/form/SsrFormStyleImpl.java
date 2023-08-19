package cn.cerc.ui.ssr.form;

import java.util.List;

import cn.cerc.ui.ssr.core.ISupplierBlock;

public interface SsrFormStyleImpl {

    ISupplierBlock getString(String title, String field);

    ISupplierBlock getCodeName(String title, String field, String... dialogFunc);

    ISupplierBlock getBoolean(String title, String field);

    ISupplierBlock getDate(String title, String field);

    ISupplierBlock getDatetime(String title, String field);

    ISupplierBlock getDateRange(String title, String beginDate, String endDate);

    ISupplierBlock getMap(String title, String field);

    ISupplierBlock getTextarea(String title, String field);

    ISupplierBlock getSubmitButton();

    List<String> items();

}
