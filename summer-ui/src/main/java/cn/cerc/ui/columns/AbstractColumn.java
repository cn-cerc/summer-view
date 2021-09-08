package cn.cerc.ui.columns;

import cn.cerc.core.Record;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.IOriginOwner;
import cn.cerc.ui.core.UIComponent;

public class AbstractColumn extends UIComponent {
    private String code;
    private String name;
    private int spaceWidth = 1;
    private Record record;

    public AbstractColumn(UIComponent owner) {
        super(owner);
        if (owner instanceof IOriginOwner) {
            this.setOrigin(((IOriginOwner) owner).getOrigin());
        }
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("AbstractColumn not support.");
    }

    public String getCode() {
        return code;
    }

    public AbstractColumn setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public AbstractColumn setName(String name) {
        this.name = name;
        return this;
    }

    public int getSpaceWidth() {
        return spaceWidth;
    }

    public AbstractColumn setSpaceWidth(int spaceWidth) {
        this.spaceWidth = spaceWidth;
        return this;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

}
