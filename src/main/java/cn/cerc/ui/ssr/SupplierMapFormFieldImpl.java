package cn.cerc.ui.ssr;

public interface SupplierMapFormFieldImpl extends SupplierMapImpl {

    default SupplierMapFormFieldImpl readonly(boolean readonly) {
        block().option("_readonly", readonly ? "1" : "");
        return this;
    };

    default SupplierMapFormFieldImpl required(boolean required) {
        block().option("_required", required ? "1" : "");
        return this;
    };

    default SupplierMapFormFieldImpl mark(String mark) {
        block().option("_mark", mark);
        return this;
    };
}
