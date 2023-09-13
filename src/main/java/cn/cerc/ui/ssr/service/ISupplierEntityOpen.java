package cn.cerc.ui.ssr.service;

import cn.cerc.mis.ado.CustomEntity;

public interface ISupplierEntityOpen extends ISupplierEntityFields {

    public VuiAbstractEntityOpenHelper<? extends CustomEntity> open();

}
