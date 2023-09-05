package cn.cerc.ui.ssr.grid;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.math.FunctionField;
import cn.cerc.mis.math.FunctionIf;
import cn.cerc.mis.math.FunctionManager;
import cn.cerc.mis.math.FunctionMath;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridCalculateField extends VuiControl implements ISupportGrid {

    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    int width = 10;
    @Column
    String align = "";
    @Column
    String calculate = "";

    @Column
    String redWhere = "";
    @Column
    String yellowWhere = "";
    @Column
    String greenWhere = "";
    @Column
    String grayWhere = "";

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text(
                String.format("<th style='width: ${_width}em' onclick=\"gridSort(this,'%s')\">${_title}</th>", field)));
        head.toMap("_width", "" + this.width);
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);

        boolean hasColorWhere = !Utils.isEmpty(redWhere) || !Utils.isEmpty(yellowWhere) || !Utils.isEmpty(greenWhere)
                || !Utils.isEmpty(grayWhere);
        String colorWhere = String.format("if(%s,red,if(%s,yellow,if(%s,green,if(%s,gray,black))))", redWhere,
                yellowWhere, greenWhere, grayWhere);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='${_align}' role='${_field}'>
                ${if _enabled_url}<a href='${callback(url)}' ${if _target}target='${_target}'${endif}>${endif}
                ${if hasColorWhere}<span style='color:${callback(color)};'>${endif}
                ${callback(val)}
                ${if hasColorWhere}</span>${endif}
                ${if _enabled_url}</a>${endif}
                </td>""", this.field)));
        body.onCallback("color", () -> {
            if (Utils.isEmpty(colorWhere))
                return "";
            DataRow current = grid.template().dataSet().current();
            FunctionManager fm = new FunctionManager();
            fm.addFunction(new FunctionMath());
            fm.addFunction(new FunctionIf());
            fm.addFunction(new FunctionField(current));
            return fm.parse(colorWhere).getString();
        });
        body.onCallback("val", () -> {
            if (Utils.isEmpty(calculate))
                return "";
            DataRow current = grid.template().dataSet().current();
            FunctionManager fm = new FunctionManager();
            fm.addFunction(new FunctionMath());
            fm.addFunction(new FunctionIf());
            fm.addFunction(new FunctionField(current));
            return fm.parse(calculate).getString();
        });
        body.option("_align", this.align);
        body.option("_field", this.field);
        body.option("_target", "");
        body.option("hasColorWhere", hasColorWhere ? "1" : "");
        body.id(bodyTitle);
        body.display(1);
        body.strict(false);
        return body;
    }

    @Override
    public SsrBlock block() {
        return body;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public ISupportGrid title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public ISupportGrid field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public ISupportGrid width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "column";
    }

}
