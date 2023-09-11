package cn.cerc.ui.ssr.service;

public interface ISupportServiceField {

    String field();
    
    void field(String field);

    String alias();

    String title();
    
    void title(String title);

    boolean required();

    void required(boolean required);

}
