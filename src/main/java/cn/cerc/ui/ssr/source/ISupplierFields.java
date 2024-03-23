package cn.cerc.ui.ssr.source;

import java.util.List;

import cn.cerc.mis.core.EntityServiceField;

public interface ISupplierFields {

    public static final int HeadInFields = 0;
    public static final int BodyInFields = 1;
    public static final int HeadOutFields = 2;
    public static final int BodyOutFields = 3;

    List<EntityServiceField> fields(int fieldsType);

}
