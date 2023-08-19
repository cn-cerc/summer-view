package cn.cerc.ui.ssr.editor;

import java.util.List;
import java.util.Optional;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.ISsrOption;
import cn.cerc.ui.ssr.SsrTemplate;
import cn.cerc.ui.ssr.ISupplierBlock;

public interface ISsrBoard extends ISsrOption {

    SsrTemplate template();

    List<String> columns();

    default ISsrBoard addColumn(String... columns) {
        for (var field : columns) {
            if (Utils.isEmpty(field))
                throw new RuntimeException("field 不允许为空");
            if (!this.columns().contains(field))
                this.columns().add(field);
        }
        return this;
    }

    default ISsrBlock addBlock(String id, String templateText) {
        var define = template();
        var ssr = new SsrBlock(templateText).setTemplate(define);
        define.addItem(id, ssr);
        return ssr;
    }

    default ISsrBoard addBlock(String id, ISsrBlock block) {
        var define = template();
        block.setTemplate(define);
        define.addItem(id, block);
        return this;
    }

    default ISsrBlock addBlock(ISupplierBlock supplier) {
        var block = supplier.request(this);
        if (supplier instanceof UIComponent item) {
            if (this instanceof UIComponent self)
                item.setOwner(self);
        }
        return block;
    }

    default Optional<ISsrBlock> getBlock(String blockId) {
        return template().get(blockId);
    }

    @Override
    default boolean strict() {
        return template().strict();
    }

    @Override
    default ISsrBoard strict(boolean strict) {
        template().strict(strict);
        return this;
    }

    @Override
    default ISsrOption option(String key, String value) {
        template().option(key, value);
        return template();
    }

    @Override
    default Optional<String> option(String key) {
        return template().option(key);
    }

}
