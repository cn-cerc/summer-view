package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

/**
 * 请改使用GridBooleanField
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Deprecated
@VuiCommonComponent
public class GridCheckBoxField extends VuiControl implements ISupportGrid {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    int fieldWidth = 5;

    public GridCheckBoxField() {
        super();
        block.option("checkbox_value_", "");
    }

    public GridCheckBoxField(String title, String field, int fieldWidth) {
        this.title = title;
        this.field = field;
        this.fieldWidth = fieldWidth;
        block.option("checkbox_value_", "");
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        var title = this.title;
        var field = this.field;
        var fieldWidth = this.fieldWidth;
        String headTitle = "head." + title;
        String bodyTitle = "body." + title;
        var ssr = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
        ssr.toMap("_width", "" + fieldWidth);
        ssr.id(headTitle);
        ssr.display(1);

        grid.addBlock(bodyTitle,
                block.text(String.format(
                        """
                                    <td align='center' role='%s'>
                                        <span><input type='checkbox' name='checkBoxName' value='${if checkbox_value_}${checkbox_value_}${else}1${endif}' ${if %s}checked ${endif}/></span>
                                    </td>
                                """,
                        field, field)));
        block.id(bodyTitle);
        block.display(1);
        return block;
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
    public String getIdPrefix() {
        return "column";
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
    public ISupportGrid width(int width) {
        this.fieldWidth = width;
        return this;
    }

}