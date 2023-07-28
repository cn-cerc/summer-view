package cn.cerc.ui.style;

import java.util.Optional;

public interface SsrOptionImpl {
    public static final String TemplateId = "templateId";
    public static final String Strict = "strict";
    public static final String Display = "option";
    public static final String Phone = "isPhone";
    public static final String Fields = "fields";
    public static final String Readonly = "readonly";

    /**
     * 
     * @param strict 是否执行严格模式，默认为 true
     * @return 返回对象本身
     */
    boolean strict();

    /**
     * 
     * @param display 取值范围：0=必选但可以调整次数；1=默认选中；2=默认不选中
     * @return 返回自身
     */
    SsrOptionImpl strict(boolean strict);

    SsrOptionImpl option(String key, String value);

    Optional<String> option(String key);

    default SsrOptionImpl display(int display) {
        this.option(Display, String.valueOf(display));
        return this;
    }

    /**
     * 
     * @param fields 返回字段列表，以逗号隔开
     * @return 返回自身
     */
    default SsrOptionImpl fields(String... fields) {
        this.option(Fields, String.join(",", fields));
        return this;
    }

    /**
     * 
     * @param phone 取值范围：true=手机环境；false=非手机环境
     * @return 返回自身
     */
    default SsrOptionImpl phone(boolean phone) {
        this.option(Phone, phone ? "1" : "");
        return this;
    }

}
