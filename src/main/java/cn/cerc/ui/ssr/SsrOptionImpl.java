package cn.cerc.ui.ssr;

import java.util.Optional;

public interface SsrOptionImpl {
    public static final String TemplateId = "templateId";
    public static final String Strict = "strict";
    public static final String Display = "option";
    public static final String Phone = "isPhone";
    public static final String Fields = "fields";
    public static final String Readonly = "readonly";

    /**
     * 是否启用严格模式
     */
    boolean strict();

    /**
     * @param strict 是否开启严格模式
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
