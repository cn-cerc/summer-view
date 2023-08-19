package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridItField extends SsrControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "序";
    @Column
    String field = "";
    @Column
    int fieldWidth = 10;

    public GridItField() {
        super();
    }

    public GridItField(String title, int fieldWidth) {
        super(null);
        this.title = title;
        this.fieldWidth = fieldWidth;
    }

    @Override
    public SsrBlock block() {
        return body;
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.templateText("<th style='width: ${_width}em'>序</th>"));
        head.id(headTitle);
        head.display(1);
        head.toMap("_width", "" + this.fieldWidth);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.templateText("<td align='center' role='_it_'>${dataset.rec}</td>"));
        body.id(bodyTitle);
        body.display(1);
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
