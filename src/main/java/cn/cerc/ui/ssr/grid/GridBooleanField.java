package cn.cerc.ui.ssr.grid;

import java.util.function.Supplier;

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
public class GridBooleanField extends VuiControl implements ISupportGrid {
    private SsrBlock block = new SsrBlock();
    Supplier<String> callBackVal;

    @Column
    String title = "";
    @Column
    String field = "checkBoxName";
    @Column
    String trueText = "是";
    @Column
    String falseText = "否";
    @Column
    int fieldWidth = 5;
    @Column(name = "只读")
    private boolean readonly = true;
    @Column(name = "自定义value")
    String customVal = "";

    public GridBooleanField() {
        super();
        init();
    }

    public GridBooleanField(String title, int fieldWidth) {
        this.title = title;
        this.fieldWidth = fieldWidth;
        init();
    }

    public GridBooleanField(String title, String field, int fieldWidth) {
        this.title = title;
        this.field = field;
        this.fieldWidth = fieldWidth;
        init();
    }

    private void init() {
        block.option("_customVal", "");
        block.option("_callBackVal", "");
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
        var ssr = grid.addBlock(headTitle,
                String.format("<th style='width: ${_width}em' onclick=\"gridSort(this,'%s')\">%s</th>", field, title));
        ssr.toMap("_width", "" + fieldWidth);
        ssr.id(headTitle);
        ssr.display(1);

        String trueText = "是";
        if (!Utils.isEmpty(this.trueText))
            trueText = this.trueText;
        String falseText = "否";
        if (!Utils.isEmpty(this.falseText))
            falseText = this.falseText;

        grid.addBlock(bodyTitle,
                block.text(String.format(
                        """
                                <td align='center' role='%s'>
                                    <span>${if _readonly}${if %s}%s${else}%s${endif}${else}
                                    <input type='checkbox' name='${_name}' value='${if _callBackVal}${callback(callBackVal)}${else}${if _customVal}%s${else}1${endif}${endif}' ${if %s}checked ${endif}/>${endif}</span>
                                </td>""",
                        field, field, trueText, falseText, customVal, field)));
        block.option("_readonly", readonly() ? "1" : "");
        block.option("_name", field);
        block.option(field, "");
        if (callBackVal != null) {
            block.option("_customVal", "");
            block.option("_callBackVal", "1");
            block.onCallback("callBackVal", callBackVal);
        }
        if (!Utils.isEmpty(customVal)) {
            block.option("_customVal", "1");
            block.option("_callBackVal", "");
        }
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

    public GridBooleanField value(Supplier<String> value) {
        this.callBackVal = value;
        return this;
    }

    public String trueText() {
        return trueText;
    }

    public GridBooleanField trueText(String trueText) {
        this.trueText = trueText;
        return this;
    }

    public String falseText() {
        return falseText;
    }

    public GridBooleanField falseText(String falseText) {
        this.falseText = falseText;
        return this;
    }

    public boolean readonly() {
        return readonly;
    }

    public GridBooleanField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

}