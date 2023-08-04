package cn.cerc.ui.ssr;

public interface SupplierFastDateFormFieldImpl extends SupplierFastDateImpl {

    default SupplierFastDateFormFieldImpl placeholder(String placeholder) {
        block().option("_placeholder", placeholder);
        return this;
    }

    default SupplierFastDateFormFieldImpl readonly(boolean readonly) {
        block().option("_readonly", readonly ? "1" : "");
        return this;
    };

    default SupplierFastDateFormFieldImpl required(boolean required) {
        block().option("_required", required ? "1" : "");
        return this;
    };

    default SupplierFastDateFormFieldImpl patten(String patten) {
        block().option("_patten", patten);
        return this;
    };

}
