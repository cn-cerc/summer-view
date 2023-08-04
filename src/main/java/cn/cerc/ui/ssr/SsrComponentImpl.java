package cn.cerc.ui.ssr;

import java.util.Optional;

public interface SsrComponentImpl extends SsrOptionImpl {

    SsrTemplate template();

    void addField(String... fields);

    default SsrBlockImpl addBlock(String id, String templateText) {
        var define = template();
        var ssr = new SsrBlock(templateText).setTemplate(define);
        define.addItem(id, ssr);
        return ssr;
    }

    default SsrComponentImpl addBlock(String id, SsrBlock block) {
        var define = template();
        block.setTemplate(define);
        define.addItem(id, block);
        return this;
    }

    default SsrBlockImpl addBlock(SupplierBlockImpl consumer) {
        return consumer.request(this);
    }

    default Optional<SsrBlockImpl> getBlock(String blockId) {
        return template().get(blockId);
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
