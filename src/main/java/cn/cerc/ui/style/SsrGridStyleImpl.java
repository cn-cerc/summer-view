package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;

public interface SsrGridStyleImpl {

    SupplierTemplateImpl getIt(String title, int fieldWidth);

    SupplierTemplateImpl getUrl(String title, int fieldWidth);

    SupplierTemplateImpl getOpera(String title, String field, int fieldWidth, String url);

    SupplierTemplateImpl getString(String title, String field, int fieldWidth);

    SupplierTemplateImpl getBoolean(String title, String field, int fieldWidth);

    SupplierTemplateImpl getOption(String title, String field, int fieldWidth, Map<String, String> map);

    List<String> items();
}
