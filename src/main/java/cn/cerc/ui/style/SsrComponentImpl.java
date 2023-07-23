package cn.cerc.ui.style;

import java.util.Optional;
import java.util.function.Consumer;

import cn.cerc.db.core.DataSet;

public interface SsrComponentImpl extends SsrOptionImpl {

    public SsrDefine getDefine();

    void addField(String... field);

    DataSet getDefaultOptions();

    void setConfig(DataSet configs);

    void onGetHtml(String field, Consumer<SsrTemplateImpl> consumer);

    default SsrTemplateImpl addTemplate(String id, String templateText) {
        var define = getDefine();
        var ssr = new SsrTemplate(templateText).setDefine(define);
        define.addItem(id, ssr);
        return ssr;
    }

    default Object addTemplate(Consumer<SsrComponentImpl> consumer) {
        if (consumer != null)
            consumer.accept(this);
        return this;
    }

    default Optional<SsrTemplateImpl> getTemplate(String templateId) {
        return getDefine().get(templateId);
    }

    @Override
    default boolean isStrict() {
        return getDefine().isStrict();
    }

    @Override
    default SsrComponentImpl setStrict(boolean strict) {
        getDefine().setStrict(strict);
        return this;
    }

    @Override
    default SsrComponentImpl setOption(String key, String value) {
        getDefine().setOption(key, value);
        return this;
    }

    @Override
    default Optional<String> getOption(String key) {
        return getDefine().getOption(key);
    }

}
