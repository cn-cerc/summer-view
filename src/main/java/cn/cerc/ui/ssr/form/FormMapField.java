package cn.cerc.ui.ssr.form;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("下拉组件")
@Deprecated
public class FormMapField extends VuiControl implements ISupportForm {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String mark = "";
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(ISupplierMap.class);
    @Column
    boolean readonly = false;
    @Column
    boolean required = false;

    public FormMapField() {
        super();
        this.mapSource.owner(this);
    }

    public FormMapField(String title, String field) {
        this.title = title;
        this.field = field;
        block.id(title).fields(field).display(1);
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String field = this.field;
        String selected = block().option("_selected").map("'%s'"::formatted).orElse(field);
        form.addBlock(title,
                block.text(String.format(
                        """
                                <li>
                                    <label for="%s"${if _mark} class='formMark'${endif}>${if _required}<font role="require">*</font>${endif}<em>%s</em></label>
                                    <div>
                                        <select id="%s" name="%s"${if _readonly} disabled${endif}>
                                        ${map.begin}
                                            <option value="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                                        ${map.end}
                                        </select>
                                    </div>
                                </li>
                                ${if _mark}
                                <li role="%s" class="liWord" style="display: none;">
                                    <mark>${_mark}</mark>
                                </li>
                                ${endif}
                                """,
                        field, title, field, field, selected, field)));
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_mark", this.mark);
        block.fields(this.field);
        return block;
    }

    public FormMapField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormMapField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormMapField mark(String mark) {
        this.mark = mark;
        return this;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ISupportForm title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String fields() {
        return this.field;
    }

    @Override
    public ISupportForm field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    public Optional<String> selected() {
        return block.option("_selected");
    }

    public FormMapField selected(String selected) {
        block.option("_selected", selected);
        return this;
    }

    public FormMapField toMap(String name, String title) {
        block.toMap(name, title);
        return this;
    }

    @Override
    public void saveEditor(RequestReader reader) {
        var oldValue = this.field;
        if (oldValue == null)
            oldValue = "";
        super.saveEditor(reader);
        var newValue = this.field;
        if (!oldValue.equals(newValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, newValue, this.getOwner().getId());
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            var obj = this.mapSource.target();
            if (obj.isPresent()) {
                ISupplierMap source = obj.get();
                source.selected().ifPresent(this::selected);
                block.toMap(source.items());
            }
            break;
        }
    }

}