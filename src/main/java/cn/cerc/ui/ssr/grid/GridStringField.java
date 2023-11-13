package cn.cerc.ui.ssr.grid;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.AlginEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class GridStringField extends VuiControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    public int fieldWidth = 10;
    @Column
    public String align = "";
    @Column
    SummaryTypeEnum summaryType = SummaryTypeEnum.无;
    @Column
    String summaryValue = "";
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);
    Supplier<String> url;
    @Column
    String target = "";

    public GridStringField() {
        super();
        body.option("_isTextField", "1");
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text(
                String.format("<th style='width: ${_width}em' onclick=\"gridSort(this,'%s')\">${_title}</th>", field)));
        head.toMap("_width", "" + this.fieldWidth);
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='${_align}' role='${_field}'>
                ${if _enabled_url}<a href='${callback(url)} '${if _target}target='${_target}' ${endif}>${endif}
                ${if _isTextField}
                ${dataset.%s}
                ${else}
                ${map.begin}${if map.key==%s}${map.value}${endif}${map.end}
                ${endif}
                ${if _enabled_url}</a>${endif}
                </td>""", this.field, this.field)));
        body.option("_align", this.align);
        body.option("_field", this.field);
        body.option("_enabled_url", url != null ? "1" : "");
        body.option("_target", Utils.isEmpty(target) ? "" : "1");
        if (url != null)
            body.onCallback("url", url);
        body.id(bodyTitle);
        body.display(1);
        body.strict(false);
        return body;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = this.mapSource.target();
            if (optMap.isPresent()) {
                ISupplierMap source = optMap.get();
                body.toMap(source.items());
                body.option("_isTextField", "");
            }
            break;
        }
    }

    public GridStringField url(Supplier<String> url) {
        this.url = url;
        return this;
    }

    public GridStringField url(String target, Supplier<String> url) {
        this.target = target;
        this.url = url;
        return this;
    }

    @Override
    public SsrBlock block() {
        return body;
    }

    public GridStringField align(AlginEnum align) {
        body.option("_align", align.name());
        return this;
    }

    public GridStringField readonly(boolean readonly) {
        body.option(ISsrOption.Readonly, readonly ? "1" : "");
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "column";
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
    public GridStringField width(int width) {
        this.fieldWidth = width;
        return this;
    }

    @Override
    public SummaryTypeEnum summaryType() {
        return summaryType;
    }

    @Override
    public void outputTotal(HtmlWriter html, DataSet dataSet) {
        String value = switch (summaryType) {
        case 计数 -> String.valueOf(dataSet.size());
        case 固定 -> summaryValue;
        default -> "";
        };
        html.print("<td>");
        html.print(value);
        html.print("</td>");
    }

    public SummaryTypeEnum getSummaryType() {
        return summaryType;
    }

    public GridStringField setSummaryType(SummaryTypeEnum summaryType) {
        this.summaryType = summaryType;
        return this;
    }

    public String getSummaryValue() {
        return summaryValue;
    }

    public GridStringField setSummaryValue(String summaryValue) {
        this.summaryValue = summaryValue;
        return this;
    }

    public ISupplierBlock toMap(Map<String, String> targetMap) {
        body.toMap(targetMap);
        body.option("_isTextField", "");
        return this;
    }

}
