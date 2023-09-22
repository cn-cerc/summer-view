package cn.cerc.ui.ssr.grid;

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
public class GridUrlField extends VuiControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "操作";
    @Column
    String context = "内容";
    @Column
    String field = "opear";
    @Column
    int fieldWidth = 5;
    @Column
    String href = "";
    @Column
    String target = "";

    @Override
    public SsrBlock request(ISsrBoard grid) {
        var title = this.title;
        String headTitle = "head." + title;
        grid.addBlock(headTitle, head.text("<th style='width: ${_width}em'>${_title}</th>"));
        head.toMap("_width", String.valueOf(this.fieldWidth));
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);
        //
        String bodyTitle = "body." + title;
        grid.addBlock(bodyTitle, body.text(String.format(
                "<td align='center' role='${_role}'><a href='%s' ${if _target}target='${_target}'${endif}>%s</a></td>",
                this.href, this.context)));
        body.id(bodyTitle);
        body.display(1);
        body.option("_role", this.field);
        body.option("_target", !Utils.isEmpty(target) ? target : "");
        body.strict(false);
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
