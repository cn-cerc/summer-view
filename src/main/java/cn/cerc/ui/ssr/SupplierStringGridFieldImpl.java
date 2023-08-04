package cn.cerc.ui.ssr;

public interface SupplierStringGridFieldImpl extends SupplierStringImpl {

    default SupplierStringGridFieldImpl width(int width) {
        block().option("_width", "" + width);
        return this;
    }

    default SupplierStringGridFieldImpl align(AlginEnum align) {
        block().option("_align", align.name());
        return this;
    }

}
