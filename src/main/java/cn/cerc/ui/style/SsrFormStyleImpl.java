package cn.cerc.ui.style;

import java.util.List;

public interface SsrFormStyleImpl {

    SupplierTemplateImpl getString(String title, String field);

    SupplierTemplateImpl getString(String title, String field, String... dialogFunc);

    SupplierTemplateImpl getCodeName(String title, String field, String... dialogFunc);

    SupplierTemplateImpl getBoolean(String title, String field);

    SupplierTemplateImpl getDate(String title, String field);

    SupplierTemplateImpl getDatetime(String title, String field);

    SupplierTemplateImpl getDateRange(String title, String beginDate, String endDate);

    SupplierTemplateImpl getMap(String title, String field);

    SupplierTemplateImpl getSubmitButton();

    List<String> items();

}
