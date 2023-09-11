package cn.cerc.ui.ssr.service;

import java.util.List;

import cn.cerc.mis.ado.CustomEntity;

public interface ISupplierEntityOpen {

    public VuiAbstractEntityOpenHelper<? extends CustomEntity> open(List<ISupportFilter> filterList);

    public List<VuiModifyField> fields();

}
