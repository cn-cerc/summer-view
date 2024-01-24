package cn.cerc.ui.ssr.grid;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.AlginEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class GridNumberField extends VuiControl implements ISupportGrid {
    private SsrBlock head = new SsrBlock();
    private SsrBlock body = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    int fieldWidth = 10;
    @Column
    String align = "";
    @Column
    String format = "";
    @Column
    SummaryTypeEnum summaryType = SummaryTypeEnum.无;
    @Column
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);
    @Column
    String formatStyle = "0.####";
    @Column
    String target = "";

    Supplier<String> url;

    public GridNumberField() {
        super();
        body.option("_isTextField", "1");
    }

    @Override
    public SsrBlock request(ISsrBoard grid) {
        String headTitle = "head." + this.title;
        grid.addBlock(headTitle, head.text(String.format(
                "<th style='width: ${_width}em' onclick=\"gridSort(this,'%s')\" title='${_title}'><div>${_title}</div></th>",
                field)));
        head.toMap("_width", "" + this.fieldWidth);
        head.toMap("_title", this.title);
        head.id(headTitle);
        head.display(1);

        String bodyTitle = "body." + this.title;
        grid.addBlock(bodyTitle, body.text(String.format("""
                <td align='${_align}' role='${_field}'>
                ${if _enabled_url}<a href='${callback(url)} '${if _target}target='${_target}' ${endif}>${endif}
                ${if _isTextField}
                ${callback(_value)}
                ${else}
                ${list.begin}${if list.index==%s}${list.value}${endif}${list.end}
                ${endif}
                ${if _enabled_url}</a>${endif}
                </td>""", this.field)));
        body.option("_align", this.align);
        body.option("_field", this.field);
        body.option("_enabled_url", url != null ? "1" : "");
        body.option("_target", Utils.isEmpty(target) ? "" : "1");
        if (url != null)
            body.onCallback("url", url);
        body.onCallback("_value", () -> {
            Optional<String> val = body.getValue(field);
            if (val.isEmpty() || Utils.isEmpty(val.get())) {
                return "0";
            } else {
                DecimalFormat df = new DecimalFormat(formatStyle);
                return df.format(new BigDecimal(val.get()));
            }
        });
        body.id(bodyTitle);
        body.display(1);
        body.strict(false);
        return body;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.listSource.init();
            break;
        case SsrMessage.InitListSourceDone:
            Optional<ISupplierList> optList = this.listSource.target();
            if (optList.isPresent()) {
                ISupplierList source = optList.get();
                body.toList(source.items());
                body.option("_isTextField", "");
            }
            break;
        }
    }

    public GridNumberField url(Supplier<String> url) {
        this.url = url;
        return this;
    }

    public GridNumberField url(String target, Supplier<String> url) {
        this.target = target;
        this.url = url;
        return this;
    }

    @Override
    public SsrBlock block() {
        return body;
    }

    public GridNumberField align(AlginEnum align) {
        this.align = align.name();
        return this;
    }

    public GridNumberField readonly(boolean readonly) {
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
    public GridNumberField width(int width) {
        this.fieldWidth = width;
        return this;
    }

    @Override
    public SummaryTypeEnum summaryType() {
        return summaryType;
    }

    @Override
    public void outputTotal(HtmlWriter html, DataSet dataSet) {
        if (summaryType == SummaryTypeEnum.无) {
            ISupportGrid.super.outputTotal(html, dataSet);
            return;
        }
        DoubleStream doubleStream = dataSet.records().stream().mapToDouble(row -> row.getDouble(field));
        double summary = switch (summaryType) {
        case 求和 -> doubleStream.sum();
        case 最大 -> doubleStream.max().orElse(0d);
        case 最小 -> doubleStream.min().orElse(0d);
        case 平均 -> doubleStream.average().orElse(0d);
        case 计数 -> doubleStream.count();
        default -> 0d;
        };
        html.print("<td>");
        if (Utils.isEmpty(format))
            html.print(String.valueOf(summary));
        else
            html.print(new DecimalFormat(format).format(summary));
        html.print("</td>");
    }

    public String getFormat() {
        return format;
    }

    public GridNumberField setFormat(String format) {
        this.format = format;
        return this;
    }

    public SummaryTypeEnum getSummaryType() {
        return summaryType;
    }

    public GridNumberField setSummaryType(SummaryTypeEnum summaryType) {
        this.summaryType = summaryType;
        return this;
    }

    public GridNumberField toList(List<String> targetList) {
        body.toList(targetList);
        body.option("_isTextField", "");
        return this;
    }

    public GridNumberField toList(Enum<?>[] enums) {
        List<String> list = new ArrayList<>();
        for (Enum<?> item : enums)
            list.add(item.name());
        return toList(list);
    }

    public GridNumberField formatStyle(String formatStyle) {
        this.formatStyle = formatStyle;
        return this;
    }

    public String formatStyle() {
        return formatStyle;
    }

}
