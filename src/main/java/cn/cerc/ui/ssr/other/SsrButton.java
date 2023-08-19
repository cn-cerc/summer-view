package cn.cerc.ui.ssr.other;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.AlignEnum;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.SsrDataRowSourceImpl;
import cn.cerc.ui.ssr.source.Binder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SsrButton extends SsrControl implements ISupportPanel {
    SsrBlock block = new SsrBlock();
    @Column
    AlignEnum align = AlignEnum.None;
    @Column
    String text = "button";
    @Column
    String href = "";
    @Column
    Binder<SsrDataRowSourceImpl> dataRow = new Binder<>(SsrDataRowSourceImpl.class);

    @Override
    public void output(HtmlWriter html) {
        var url = new SsrBlock(this.href);
        Optional<SsrDataRowSourceImpl> optDataRow = this.dataRow.target();
        if (optDataRow.isPresent())
            url.dataRow(optDataRow.get().dataRow());
        block.text(String.format("""
                <div id="%s" class="bottomBotton"><a href="%s">${_text}</a></div>""", this.getId(), url.html()));
        block.option("_text", this.text);
        html.print(block.html());
    }

    @Override
    public String getIdPrefix() {
        return "button";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            dataRow.owner(this);
            dataRow.init();
            break;
        }
    }

}