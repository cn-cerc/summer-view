package cn.cerc.ui.ssr;

public interface SupplierTextareaFormFieldImpl extends SupplierStringImpl {

    default SupplierTextareaFormFieldImpl placeholder(String placeholder) {
        block().option("_placeholder", placeholder);
        return this;
    }

    @Override
    default SupplierTextareaFormFieldImpl readonly(boolean readonly) {
        SupplierStringImpl.super.readonly(readonly);
        return this;
    };

    default SupplierTextareaFormFieldImpl required(boolean required) {
        block().option("_required", required ? "1" : "");
        return this;
    };

    default SupplierTextareaFormFieldImpl mark(String mark) {
        block().option("_mark", mark);
        return this;
    };
}
