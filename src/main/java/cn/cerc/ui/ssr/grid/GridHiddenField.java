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
public class GridHiddenField extends VuiControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    int fieldWidth = 0;
    @Column
    boolean readonly = false;
    
    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text(String.format("""
                <th style='${if _width}width: ${_width}em;${else} display: none;${endif}'
                onclick=\"gridSort(this,'%s')\">${_title}</th>""", field)));
        head.option("_title", this.title);
        head.toMap("_width", "" + this.fieldWidth);
        head.id(headTitle);
        head.option("option", null);
        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='left' ${if not _width} style="display: none;" ${endif} role='${_field}'>
                    ${if _readonly}${dataset.%s}
                    ${else}<input type="text" name="${_field}" id="${dataset.rec}_0" value="${dataset.%s}"/>
                    ${endif}</td>""", this.field, this.field)));
        body.option("_field", this.field);
        body.toMap("_width", "" + this.fieldWidth);
        body.option("_readonly", this.readonly ? "1" : "");
        body.option("option", null);
        body.id(bodyTitle);
        grid.addColumn(title);
        return body;
    }
    
    @Override
    public SsrBlock block() {
        return body;
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

    public int width() {
        return this.fieldWidth;
    }

    @Override
    public GridHiddenField width(int width) {
        this.fieldWidth = width;
        return this;
    }
    
    public GridHiddenField readonly() {
        this.readonly = true;
        return this;
    }
}

