package cn.cerc.ui.style;

import java.util.Optional;

public interface SsrOptionImpl {

    /**
     * 
     * @param strict 是否执行严格模式，默认为 true
     * @return 返回对象本身
     */
    Object setStrict(boolean strict);

    /**
     * 
     * @return 返回解析模式，默认为严格模式
     */
    boolean isStrict();

    SsrOptionImpl setOption(String key, String value);

    Optional<String> getOption(String key);

}
