package cn.cerc.ui.ssr;

import java.util.List;
import java.util.Map;

public interface SsrGridStyleImpl {

    ISupplierBlock getIt(String title, int fieldWidth);

    ISupplierBlock getOpera(int fieldWidth);

    ISupplierBlock getString(String title, String field, int fieldWidth);

    ISupplierBlock getDate(String title, String field);

    ISupplierBlock getBoolean(String title, String field, int fieldWidth);

    ISupplierBlock getCheckBox(String title, String field, int fieldWidth);

    ISupplierBlock getMap(String title, String field, int fieldWidth, Map<String, String> map);

    List<String> items();
}
