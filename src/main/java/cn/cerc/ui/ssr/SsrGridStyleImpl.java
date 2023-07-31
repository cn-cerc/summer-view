package cn.cerc.ui.ssr;

import java.util.List;
import java.util.Map;

public interface SsrGridStyleImpl {

    SupplierBlockImpl getIt(String title, int fieldWidth);

    SupplierBlockImpl getOpera(int fieldWidth);

    SupplierBlockImpl getString(String title, String field, int fieldWidth);

    SupplierBlockImpl getDate(String title, String field);

    SupplierBlockImpl getBoolean(String title, String field, int fieldWidth);

    SupplierBlockImpl getOption(String title, String field, int fieldWidth, Map<String, String> map);

    List<String> items();
}
