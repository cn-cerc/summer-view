package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class GridOperaField extends VuiControl implements ISupportGrid {
    private SsrBlock block = new SsrBlock();
    private String title = "操作";
    @Column
    String content = "内容";
    @Column
    int fieldWidth = 4;

    public GridOperaField() {
        super();
    }

    public GridOperaField(int fieldWidth) {
        super(null);
        this.fieldWidth = fieldWidth;
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + title;
        String bodyTitle = "body." + title;
        int fieldWidth = this.fieldWidth;
        var ssr = grid.addBlock(headTitle, String.format("""
                <th style='width: ${_width}em' onclick="gridSort(this,'_opera_')">
                <div ${if templateId}role='ssrconfig' data-templateid='${templateId}'${endif}>%s</div>
                </th>
                """, title));
        ssr.toMap("_width", "" + fieldWidth);
        ssr.option("templateId", "");
        ssr.option("templateConfigImg", "");
        ssr.id(headTitle);

        grid.addBlock(bodyTitle,
                block.text("<td align='center' role='_opera_'><a href='${callback(url)}'>${_content}</a></td>"));
        block.display(1);
        block.id(bodyTitle);
        block.option("_content", this.content);
        return block;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ISupportGrid title(String title) {
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "column";
    }

    @Override
    public String field() {
        return "";
    }

    @Override
    public ISupportGrid field(String field) {
        return this;
    }

    @Override
    public int width() {
        return this.fieldWidth;
    }

    @Override
    public ISupportGrid width(int width) {
        this.fieldWidth = width;
        return this;
    }

}
