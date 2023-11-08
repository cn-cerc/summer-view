package cn.cerc.ui.ssr.block;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class BlockNumberField extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);
    @Column
    String formatStyle = "0.####";

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
                                        <label>%s</label>
                                        ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _isTextField}${callback(_value)}${else}${list.begin}${if list.index==%s}${list.value}${endif}${list.end}${endif}${if _enabled_url}</a>${else}</span>${endif}
                                    </div>
                        """,
                title, field, field, field));
        block.option("_enabled_url", url != null ? "1" : "");
        if (url != null)
            block.onCallback("url", url);
        block.onCallback("_value", () -> {
            Optional<String> val = block.getValue(field);
            if (val.isEmpty() || Utils.isEmpty(val.get())) {
                return "0";
            } else {
                DecimalFormat df = new DecimalFormat(formatStyle);
                return df.format(new BigDecimal(val.get()));
            }
        });
        return block;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.listSource.init();
            break;
        case SsrMessage.InitListSourceDone:
            Optional<ISupplierList> optList = this.listSource.target();
            if (optList.isPresent()) {
                ISupplierList source = optList.get();
                block.toList(source.items());
                block.option("_isTextField", "");
            }
            break;
        }
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

    public BlockNumberField formatStyle(String formatStyle) {
        this.formatStyle = formatStyle;
        return this;
    }

    public String formatStyle() {
        return formatStyle;
    }

}
