package cn.cerc.ui.ssr;

import java.util.List;

public interface SsrFormStyleImpl {

    SupplierBlockImpl getString(String title, String field);

    SupplierBlockImpl getCodeName(String title, String field, String... dialogFunc);

    SupplierBlockImpl getBoolean(String title, String field);

    SupplierBlockImpl getDate(String title, String field);

    SupplierBlockImpl getDatetime(String title, String field);

    SupplierBlockImpl getDateRange(String title, String beginDate, String endDate);

    SupplierBlockImpl getMap(String title, String field);

    SupplierBlockImpl getTextarea(String title, String field);

    SupplierBlockImpl getSubmitButton();

    List<String> items();

}
