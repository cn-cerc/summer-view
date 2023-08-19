package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridBooleanField extends SsrControl implements ISupportGrid {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String trueText = "是";
    @Column
    String falseText = "否";
    @Column
    int fieldWidth = 5;

    public GridBooleanField() {
        super();
    }

    public GridBooleanField(String title, String field, int fieldWidth) {
        this.title = title;
        this.field = field;
        this.fieldWidth = fieldWidth;
    }

    @Override
    public ISsrBlock block() {
        return block;
    }

    @Override
    public ISsrBlock request(ISsrBoard grid) {
        var title = this.title;
        var field = this.field;
        var fieldWidth = this.fieldWidth;
        String headTitle = "head." + title;
        String bodyTitle = "body." + title;
        var ssr = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
        ssr.toMap("_width", "" + fieldWidth);
        ssr.id(headTitle);
        ssr.display(1);

        String trueText = "是";
        if (!Utils.isEmpty(this.trueText))
            trueText = this.trueText;
        String falseText = "否";
        if (!Utils.isEmpty(this.falseText))
            falseText = this.falseText;

        grid.addBlock(bodyTitle, block.templateText(String.format("""
                <td align='center' role='%s'>
                    <span>
                        ${if %s}
                        %s
                        ${else}
                        %s
                        ${endif}
                    </span>
                </td>""", field, field, trueText, falseText)));
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