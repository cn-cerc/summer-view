package cn.cerc.ui.ssr.grid;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.AlginEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridStringField extends VuiControl implements ISupportGrid {
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
        body.option("_list", "");
        body.option("_map", "");
        body.option("_select", "");
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text("<th style='width: ${_width}em'>${_title}</th>"));
        head.option("_width", "" + this.fieldWidth);
        head.option("_title", this.title);
        head.id(headTitle);
        head.display(1);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format(
                "<td align='${_align}' role='${_field}'>${if _select}${if _map}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}${list.begin}${if list.index==%s}${list.value}${endif}${list.end}${endif}${else}${if _enabled_url}<a href='${callback(url)}'>${endif}${dataset.%s}${if _enabled_url}</a>${endif}${endif}</td>",
                field, field, field)));
        body.option("_align", this.align);
        body.option("_field", this.field);
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
    public SsrBlock block() {
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

    public GridStringField toList(String... values) {
        body.toList(values);
        body.option("_list", "1");
        body.option("_select", "1");
        return this;
    }

    public GridStringField toList(List<String> list) {
        body.toList(list);
        body.option("_list", "1");
        body.option("_select", "1");
        return this;
    }

    public GridStringField toList(Enum<?>[] enums) {
        body.toList(enums);
        body.option("_list", "1");
        body.option("_select", "1");
        return this;
    }

    public GridStringField toMap(String key, String value) {
        body.toMap(key, value);
        body.option("_map", "1");
        body.option("_select", "1");
        return this;
    }

    public GridStringField toMap(Map<String, String> map) {
        body.toMap(map);
        body.option("_map", "1");
        body.option("_select", "1");
        return this;
    }

}
