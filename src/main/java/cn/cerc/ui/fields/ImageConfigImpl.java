package cn.cerc.ui.fields;

public interface ImageConfigImpl {

    String getClassProperty(Class<?> owner, String packageId, String key, String defaultValue);

    String getCommonFile(String src);

    String getProductFile(String src);

}
