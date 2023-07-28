package cn.cerc.ui.style;

public interface SupplierFieldImpl extends SupplierTemplateImpl {
    SupplierFieldImpl readonly(boolean readonly);

    SupplierFieldImpl required(boolean required);

    SupplierFieldImpl placeholder(String placeholder);

    SupplierFieldImpl patten(String patten);

    SupplierFieldImpl dialog(String... dialogFunc);

    SupplierFieldImpl autofocus(boolean autofocus);

    SupplierFieldImpl type(String type);

    SupplierFieldImpl fieldBefore(String fieldBefore);
}
