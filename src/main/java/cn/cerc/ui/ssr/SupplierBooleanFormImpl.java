package cn.cerc.ui.ssr;

public interface SupplierBooleanFormImpl extends SupplierBooleanImpl {

    default SupplierBooleanFormImpl mark(String mark) {
        block().option("_mark", mark);
        return this;
    }
}
