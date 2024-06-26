package cn.cerc.ui.ssr.block;

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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class BlockOperaField extends VuiControl implements ISupportBlock {
    private SsrBlock block = new SsrBlock();
    Supplier<String> callBackUrl;
    @Column
    String title = "操作";
    @Column
    String field = "";
    @Column
    String href = "";
    @Column
    String text = "内容";

    public BlockOperaField(String field) {
        super();
        this.field = field;
        init();
    }

    public BlockOperaField(Supplier<String> callBackUrl) {
        super();
        this.callBackUrl = callBackUrl;
        init();
    }

    public BlockOperaField() {
        super();
        init();
    }

    public void init() {
        block.display(1);
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format(
                """
                        <div role='opera'>
                            <a ${if _href}href='%s'${else}${if _callBackUrl}href='${callback(callBackUrl)}'${else}href='${%s}'${endif}${endif}>${_text}</a>
                        </div>
                        """,
                href, field));
        block.option("_callBackUrl", callBackUrl != null ? "1" : "");
        block.option("_text", text);
        if (callBackUrl != null)
            block.onCallback("callBackUrl", callBackUrl);
        block.option("_href", Utils.isEmpty(href) ? "" : "1");
        return block;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public BlockOperaField field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public BlockOperaField title(String title) {
        this.title = title;
        return this;
    }

    public BlockOperaField href(String href) {
        this.href = href;
        return this;
    }

    public String href() {
        return href;
    }

    public BlockOperaField text(String text) {
        this.text = text;
        return this;
    }

    public String text() {
        return text;
    }

}
