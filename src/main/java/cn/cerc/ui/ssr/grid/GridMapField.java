package cn.cerc.ui.ssr.grid;

import java.util.Map;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.VuiMapService;

@Deprecated
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GridMapField extends VuiControl implements ISupportGrid {
    private SsrBlock block = new SsrBlock();
    private Map<String, String> map;
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String mapSource = "";
    @Column
    int fieldWidth = 10;

    public GridMapField() {
        super();
    }

    public GridMapField(String title, String field, int fieldWidth, Map<String, String> map) {
        super(null);
        this.title = title;
        this.field = field;
        this.fieldWidth = fieldWidth;
        this.map = map;
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
        var head = grid.addBlock(headTitle,
                String.format("<th style='width: ${_width}em' onclick=\"gridSort(this,'%s')\">%s</th>", field, title));
        head.toMap("_width", "" + fieldWidth);
        head.id(headTitle);
        head.display(1);

        grid.addBlock(bodyTitle, block.text(String.format("""
                <td role='%s'>${if readonly}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}
                <select>
                ${map.begin}
                <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                ${map.end}
                </select>
                ${endif}</td>
                """, field, field, field)));
        block.option("readonly", "true");
        block.id(bodyTitle);
        block.display(1);
        if (map != null)
            map.forEach((key, value) -> block.toMap(key, value));
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
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitMapSourceDone:
            var obj = this.canvas().getMember(this.mapSource, VuiMapService.class);
            if (obj.isPresent()) {
                VuiMapService source = obj.get();
                block.toMap(source.items());
            }
            break;
        }
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