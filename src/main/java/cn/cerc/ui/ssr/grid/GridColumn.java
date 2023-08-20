package cn.cerc.ui.ssr.grid;

import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridColumn extends VuiContainer<ISupportGridColumn> implements ISupportGrid, Supplier<String> {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    String field;
    @Column
    public int fieldWidth = 10;

    public GridColumn() {
        super();
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text("<th style='width: ${_width}em'>${_title}</th>"));
        head.toMap("_width", "" + this.fieldWidth);
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='${_align}' role='${_field}'>
                ${callback(columns)}
                </td>
                """, this.field)));
        head.toMap("_field", this.field);
        body.id(bodyTitle);
        body.display(1);
        body.strict(false);
        body.onCallback("columns", this);
        return body;
    }

    @Override
    public String get() {
        StringBuffer sb = new StringBuffer();
        for (var column : this) {
            if (column instanceof ISupportGridColumn impl) {
                if (this.getOwner() instanceof UISsrGrid grid)
                    impl.dataSet(grid.dataSet());
            }
            sb.append(column.toString());
        }
        return sb.toString();
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
