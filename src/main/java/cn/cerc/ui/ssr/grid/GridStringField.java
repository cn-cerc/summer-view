package cn.cerc.ui.ssr.grid;

import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.AlginEnum;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.ISsrOption;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridStringField extends SsrControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    public int fieldWidth = 10;
    @Column
    public String align = "";

    public GridStringField() {
        super();
    }

    @Override
    public ISsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.templateText("<th style='width: ${_width}em'>${_title}</th>"));
        head.toMap("_width", "" + this.fieldWidth);
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.templateText(String.format(
                "<td align='${_align}' role='${_field}'>${if _enabled_url}<a href='${callback(url)}'>${endif}${dataset.%s}${if _enabled_url}</a>${endif}</td>",
                this.field)));
        head.toMap("_align", this.align);
        head.toMap("_field", this.field);
        body.id(bodyTitle);
        body.display(1);
        body.strict(false);
        return body;
    }

    public GridStringField url(Supplier<String> url) {
        body.option("_enabled_url", "1");
        body.onCallback("url", url);
        return this;
    }

    @Override
    public ISsrBlock block() {
        return body;
    }

    public GridStringField align(AlginEnum align) {
        body.option("_align", align.name());
        return this;
    }

    public GridStringField readonly(boolean readonly) {
        body.option(ISsrOption.Readonly, readonly ? "1" : "");
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "column";
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ISupportGrid title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String field() {
        return this.field;
    }

    @Override
    public ISupportGrid field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public int width() {
        return this.fieldWidth;
    }

    @Override
    public GridStringField width(int width) {
        this.fieldWidth = width;
        return this;
    }

}
