package cn.cerc.ui.ssr;

public interface SupplierStringFormFieldImpl extends SupplierStringImpl {

    default SupplierStringFormFieldImpl placeholder(String placeholder) {
        block().option("_placeholder", placeholder);
        return this;
    }

    @Override
    default SupplierStringFormFieldImpl readonly(boolean readonly) {
        SupplierStringImpl.super.readonly(readonly);
        return this;
    };

    default SupplierStringFormFieldImpl required(boolean required) {
        block().option("_required", required ? "1" : "");
        return this;
    };

    default SupplierStringFormFieldImpl autofocus(boolean autofocus) {
        block().option("_autofocus", autofocus ? "1" : "");
        return this;
    };

    default SupplierStringFormFieldImpl patten(String patten) {
        block().option("_patten", patten);
        return this;
    };

    default SupplierStringFormFieldImpl mark(String mark) {
        block().option("_mark", mark);
        return this;
    };

}
