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

    SsrComponentImpl addTemplate(String id, String templateText);

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
