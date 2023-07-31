package cn.cerc.ui.ssr;

import java.util.Optional;
import java.util.function.Consumer;

import cn.cerc.db.core.DataSet;

public interface SsrComponentImpl extends SsrOptionImpl {

    public SsrTemplate template();

    void addField(String... fields);

    DataSet getDefaultOptions();

    void setConfig(DataSet configs);

    void onGetHtml(String field, Consumer<SsrBlockImpl> consumer);

    default SsrBlockImpl addBlock(String id, String templateText) {
        var define = template();
        var ssr = new SsrBlock(templateText).setTemplate(define);
        define.addItem(id, ssr);
        return ssr;
    }

    default SsrBlockImpl addBlock(SupplierBlockImpl consumer) {
        return consumer.request(this);
    }

    default Optional<SsrBlockImpl> getBlock(String templateId) {
        return template().get(templateId);
    }

    @Override
    default boolean strict() {
        return template().strict();
    }

    @Override
    default SsrComponentImpl strict(boolean strict) {
        template().strict(strict);
        return this;
    }

    @Override
    default SsrOptionImpl option(String key, String value) {
        template().option(key, value);
        return template();
    }

    @Override
    default Optional<String> option(String key) {
        return template().option(key);
    }

}
