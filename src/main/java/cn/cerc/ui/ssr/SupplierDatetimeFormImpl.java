package cn.cerc.ui.ssr;

public interface SupplierDatetimeFormImpl extends SupplierDatetimeImpl {

    default SupplierDatetimeFormImpl placeholder(String placeholder) {
        block().option("_placeholder", placeholder);
        return this;
    }

    default SupplierDatetimeFormImpl readonly(boolean readonly) {
        block().option("_readonly", readonly ? "1" : "");
        return this;
    };

    default SupplierDatetimeFormImpl required(boolean required) {
        block().option("_required", required ? "1" : "");
        return this;
    };

    default SupplierDatetimeFormImpl patten(String patten) {
        block().option("_patten", patten);
        return this;
    };

}
