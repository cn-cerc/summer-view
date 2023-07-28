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
     * 请改用 strict
     * 
     * @return
     */
    @Deprecated
    boolean isStrict();

    /**
     * 请改用 strict
     * 
     */
    @Deprecated
    SsrOptionImpl setStrict(boolean strict);

    /**
     * 请改使用 option
     */
    @Deprecated
    SsrOptionImpl setOption(String key, String value);

    /**
     * 请改使用 option
     */
    @Deprecated
    Optional<String> getOption(String key);

    default SsrOptionImpl option(String key, String value) {
        return setOption(key, value);
    }

    default Optional<String> option(String key) {
        return getOption(key);
    }

    /**
     * 
     * @param strict 是否执行严格模式，默认为 true
     * @return 返回对象本身
     */
    default SsrOptionImpl strict(boolean strict) {
//        this.option(Strict, strict ? "1" : "");
        this.setStrict(strict);
        return this;
    }

    /**
     * 
     * @return 返回解析模式，默认为严格模式
     */
    default boolean strict() {
//        return !"".equals(this.option(Strict).orElse("1"));
        return this.isStrict();
    }

    /**
     * 
     * @param display 取值范围：0=必选但可以调整次数；1=默认选中；2=默认不选中
     * @return 返回自身
     */
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
     * @param readonly 取值范围：true/false
     * @return 返回自身
     */
    default SsrOptionImpl readonly(boolean readonly) {
        this.option(Readonly, readonly ? "1" : "");
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
