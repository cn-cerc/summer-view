package cn.cerc.ui.ssr.block;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockStringField extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);
    @Column
    Boolean hideTitle = false;

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
                                        <label>%s</label>
                                        ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _isTextField}${%s}${else}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${endif}${if _enabled_url}</a>${else}</span>${endif}
                                    </div>
                        """,
               this.hideTitle ? "" : title, field, field, field, field));
        block.option("_enabled_url", url != null ? "1" : "");
        if (url != null)
            block.onCallback("url", url);
        return block;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = this.mapSource.target();
            if (optMap.isPresent()) {
                ISupplierMap source = optMap.get();
                block.toMap(source.items());
                block.option("_isTextField", "");
            }
            break;
        }
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    public BlockStringField url(Supplier<String> url) {
        this.url = url;
        return this;
    }

    public BlockStringField hideTitle(Boolean hideTitle) {
        this.hideTitle = hideTitle;
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

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public ISupportBlock title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public ISupportBlock field(String field) {
        this.field = field;
        return this;
    }

}
