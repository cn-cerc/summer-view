package cn.cerc.ui.ssr.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.SsrUtils;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class GridGroup extends VuiContainer<ISupportGrid> implements ISupportGrid, Supplier<String>, ISsrBoard {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    private SsrTemplate template = new SsrTemplate();
    @Column
    String title = "";
    String field;
    @Column
    public int fieldWidth = 10;
    @Column(name = "是否展示描述")
    boolean showTitle = false;

    public GridGroup() {
        super();
    }

    public GridGroup(String title, String field) {
        super();
        this.title = title;
        this.field = field;
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
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='${_align}' role='${_field}'>
                ${callback(columns)}
                </td>
                """, this.field)));
        body.option("_field", this.field);
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
            if (column instanceof ISupportGrid impl) {
                impl.request(this);
                if (this.getOwner() instanceof VuiGrid grid)
                    impl.block().dataSet(grid.dataSet());
                sb.append("<span>");
                if (showTitle)
                    sb.append(impl.title() + "：");
                sb.append(SsrUtils.extractTagContent(impl.block().html(), "td"));
                sb.append("</span>");
                sb.append("<br/>");
            }
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

    @Override
    public SsrTemplate template() {
        return template;
    }

    @Override
    public List<String> columns() {
        return new ArrayList<String>(template.items().keySet());
    }

    @Override
    public ISsrBoard addBlock(String id, SsrBlock block) {
        template.addItem(id, block);
        return this;
    }

    public boolean showTitle() {
        return showTitle;
    }

    public GridGroup showTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

}
