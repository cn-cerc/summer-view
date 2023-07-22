package cn.cerc.ui.style;

import java.util.Optional;
import java.util.function.Consumer;

import cn.cerc.db.core.DataSet;

public interface SsrComponentImpl extends SsrStrictImpl {

    public SsrDefine getDefine();

    void addField(String... field);

    DataSet getDefaultOptions();

    void setConfig(DataSet configs);

    void onGetHtml(String field, Consumer<SsrTemplateImpl> consumer);

    default SsrComponentImpl addTemplate(String id, String templateText) {
        getDefine().addItem(id, new SsrTemplate(templateText));
        return this;
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

}
