package cn.cerc.ui.ssr;

public interface SupplierFieldImpl extends SupplierBlockImpl {
    SupplierFieldImpl readonly(boolean readonly);

    SupplierFieldImpl required(boolean required);

    SupplierFieldImpl placeholder(String placeholder);

    SupplierFieldImpl patten(String patten);

    SupplierFieldImpl dialog(String... dialogFunc);

    SupplierFieldImpl autofocus(boolean autofocus);

    SupplierFieldImpl type(String type);

    SupplierFieldImpl fieldBefore(String fieldBefore);
}
