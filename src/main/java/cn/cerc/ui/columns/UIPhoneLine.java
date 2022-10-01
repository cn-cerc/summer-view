package cn.cerc.ui.columns;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.IReadonlyOwner;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneLine extends UIComponent implements IReadonlyOwner {
    private List<UIPhoneLineCell> cells = new ArrayList<>();
    private UIComponent attachLine;
    private boolean readonly;
    private DataRow record;

    public DataRow getRecord() {
        return record;
    }

    public void setRecord(DataRow record) {
        this.record = record;
    }

    public UIPhoneLine(UIComponent owner) {
        super(owner);
        if (owner instanceof IReadonlyOwner) {
            this.setReadonly(((IReadonlyOwner) owner).isReadonly());
        }
    }

    public UIComponent cell(int index) {
        if (this.getOrigin() instanceof IForm) {
            IForm form = (IForm) this.getOrigin();
            if (form.getClient().isPhone()) {
                return cells.get(index);
            } else {
                return this.getOwner();
            }
        } else {
            return cells.get(index);
        }
    }

    /**
     * 建立相应的格子，并设置每个格式的宽度
     * 
     * @param percents 数组，其各值加起来必须等于100
     */
    public void buildCells(int... percents) {
        int total = 0;
        for (int percent : percents) {
            cells.add(new UIPhoneLineCell(this, percent));
            total += percent;
        }
        if (total != 100)
            throw new RuntimeException("int[] args total <> 100.");
    }

    @Override
    public void output(HtmlWriter html) {
        for (UIPhoneLineCell cell : this.cells) {
            cell.setRecord(record);
            cell.output(html);
        }
    }

    private class UIPhoneLineCell extends UIComponent implements IReadonlyOwner {
        // 在UIBlock中，宽度所占百分比
        private int percent;
        private DataRow record;
        private boolean readonly;

        public UIPhoneLineCell(UIComponent owner, int percent) {
            super(owner);
            if (owner instanceof IReadonlyOwner) {
                this.setReadonly(((IReadonlyOwner) owner).isReadonly());
            }
            this.percent = percent;
        }

        @Override
        public void output(HtmlWriter html) {
            html.print("<span class=\"field\"  style='width:%d%%'>", this.percent);
            for (UIComponent item : this.children()) {
                if (item instanceof IColumn) {
                    IColumn column = (IColumn) item;
                    if (item instanceof IDataColumn)
                        ((IDataColumn) column).setRecord(record);
                    column.outputCell(html);
                }
            }
            html.println("</span>");
        }

        @Override
        public UIComponent addComponent(UIComponent owner) {
            super.addComponent(owner);
            if (attachLine != null) {
                attachLine.addComponent(owner);
            }
            return this;
        }

        public void setRecord(DataRow record) {
            this.record = record;
        }

        @Override
        public boolean isReadonly() {
            return readonly;
        }

        @Override
        public UIPhoneLineCell setReadonly(boolean readonly) {
            this.readonly = readonly;
            return this;
        }

    }

    public UIComponent getAttachLine() {
        return attachLine;
    }

    public void setAttachLine(UIComponent attachLine) {
        this.attachLine = attachLine;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public UIPhoneLine setReadonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

}
