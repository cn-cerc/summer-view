package cn.cerc.ui.ssr.source;

import java.lang.reflect.Field;
import java.util.Set;

public interface ISupplierFields {

    public static final int HeadInFields = 0;
    public static final int BodyInFields = 1;
    public static final int HeadOutFields = 2;
    public static final int BodyOutFields = 3;

    Set<Field> fields(int fieldsType);

}