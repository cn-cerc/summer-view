package cn.cerc.ui.ssr.grid;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.AlginEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridNumberField extends VuiControl implements ISupportGrid {
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
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);

    public GridNumberField() {
        super();
        body.option("_isTextField", "1");
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
                ${if _enabled_url}<a href='${callback(url)}'>${endif}
                ${if _isTextField}
                ${dataset.%s}
                ${else}
                ${list.begin}${if list.index==%s}${list.value}${endif}${list.end}
                ${endif}
                ${if _enabled_url}</a>${endif}
                </td>""", this.field, this.field)));
        head.toMap("_align", this.align);
        head.toMap("_field", this.field);
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
        body.option("_enabled_url", "1");
        body.onCallback("url", url);
        return this;
    }

    @Override
    public SsrBlock block() {
        return body;
    }

    public GridNumberField align(AlginEnum align) {
        body.option("_align", align.name());
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

    public ISupplierBlock toList(List<String> targetList) {
        body.toList(targetList);
        body.option("_isTextField", "");
        return this;
    }

}
