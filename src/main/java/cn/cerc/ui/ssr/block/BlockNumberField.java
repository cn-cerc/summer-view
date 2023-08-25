package cn.cerc.ui.ssr.block;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.Column;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockNumberField implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title;
    @Column
    String field;
    Supplier<String> url;

    public BlockNumberField(String title, String field) {
        super();
        this.title = title;
        this.field = field;
        init();
    }

    public BlockNumberField() {
        super();
        init();
    }

    private void init() {
        block.option("_isTextField", "1");
        block.option("_ratio", "1");
        block.display(1);
    }

    @Override
    public SsrBlock request(ISsrBoard line) {
        block.text(String.format(
                """
                                    <div style='flex: ${_ratio};'>
                                        <label for='%s'>%s</label>
                                        ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _isTextField}${%s}${else}${list.begin}${if list.index==%s}${list.value}${endif}${list.end}${endif}${if _enabled_url}</a>${else}</span>${endif}
                                    </div>
                        """,
                field, title, field, field, field, field));
        block.option("_enabled_url", url != null ? "1" : "");
        if (url != null)
            block.onCallback("url", url);
        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    public BlockNumberField toList(String... values) {
        block.toList(values);
        block.option("_isTextField", "");
        return this;
    }

    public BlockNumberField toList(List<String> list) {
        block.toList(list);
        block.option("_isTextField", "");
        return this;
    }

    public BlockNumberField toList(Enum<?>[] enums) {
        block.toList(enums);
        block.option("_isTextField", "");
        return this;
    }

}
