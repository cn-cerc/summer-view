package cn.cerc.ui.ssr.block;

import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.Column;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class BlockStringField implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title;
    @Column
    String field;
    Supplier<String> url;

    public BlockStringField(String title, String field) {
        super();
        this.title = title;
        this.field = field;
        init();
    }

    public BlockStringField() {
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
                                        ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _isTextField}${%s}${else}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${endif}${if _enabled_url}</a>${else}</span>${endif}
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

    public BlockStringField url(Supplier<String> url) {
        this.url = url;
        return this;
    }

    public BlockStringField toMap(String key, String value) {
        block.toMap(key, value);
        block.option("_isTextField", "");
        return this;
    }

    public BlockStringField toMap(Map<String, String> map) {
        block.toMap(map);
        block.option("_isTextField", "");
        return this;
    }

}
