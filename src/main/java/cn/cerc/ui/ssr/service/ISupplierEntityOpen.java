package cn.cerc.ui.ssr.service;

import java.util.List;

import cn.cerc.mis.ado.CustomEntity;

public interface ISupplierEntityOpen {

    public AbstractEntityOpenHelper<? extends CustomEntity> open(List<ISupportFilter> filterList);

    public List<VuiModifyField> fields();

}
