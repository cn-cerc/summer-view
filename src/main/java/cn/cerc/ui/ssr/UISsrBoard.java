package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISsrBoard extends UIComponent implements SsrComponentImpl {
//    private static final Logger log = LoggerFactory.getLogger(UISsrBoard.class);
    private static final int Max_slot = 8;
    private SsrBlockImpl cpu;
    private List<SsrBlockImpl> items = new ArrayList<>();
    private SsrTemplate template = new SsrTemplate("");

    public UISsrBoard(UIComponent owner) {
        super(owner);
        this.cpu(new SsrBlock("""
                ${callback(slot0)}
                ${callback(slot1)}
                ${callback(slot2)}
                ${callback(slot3)}
                ${callback(slot4)}
                ${callback(slot5)}
                ${callback(slot6)}
                ${callback(slot7)}
                """));
        for (int i = 0; i < Max_slot; i++)
            items.add(new SsrBlock("slot" + i));
    }

    @Override
    public void output(HtmlWriter html) {
        Objects.requireNonNull(cpu);

        for (SsrBlockImpl slot : items) {
            if (slot instanceof SsrBlock soltBlock)
                soltBlock.setTemplate(template);
            cpu.onCallback(slot.id(), () -> slot.getHtml());
        }

        if (cpu instanceof SsrBlock block)
            block.setTemplate(template);
        html.print(cpu.getHtml());
    }

    /**
     * 请改使用 columns
     * 
     * @return
     */
    @Deprecated
    public List<String> fields() {
        return columns();
    }

    @Override
    public List<String> columns() {
        throw new RuntimeException("此对象不支持此功能");
    }

    public UISsrBoard cpu(SsrBlockImpl cpu) {
        Objects.requireNonNull(cpu);
        this.cpu = cpu;
        for (int i = 0; i < Max_slot; i++)
            cpu.onCallback("slot" + i, () -> "");
        return this;
    }

    private UISsrBoard updateSlot(SsrBlockImpl slot, int index) {
        items.set(index, slot);
        if (slot instanceof SsrBlock block)
            block.setTemplate(this.template);
        slot.id("slot" + index);
        return this;
    }

    protected UISsrBoard slot0(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 0);
    }

    protected UISsrBoard slot1(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 1);
    }

    protected UISsrBoard slot2(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 2);
    }

    protected UISsrBoard slot3(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 3);
    }

    protected UISsrBoard slot4(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 4);
    }

    protected UISsrBoard slot5(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 5);
    }

    protected UISsrBoard slot6(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 6);
    }

    protected UISsrBoard slot7(SupplierBlockImpl slot) {
        return updateSlot(slot.request(this), 7);
    }

    protected SsrBlockImpl slot0() {
        return items.get(0);
    }

    protected SsrBlockImpl slot1() {
        return items.get(1);
    }

    protected SsrBlockImpl slot2() {
        return items.get(2);
    }

    protected SsrBlockImpl slot3() {
        return items.get(3);
    }

    protected SsrBlockImpl slot4() {
        return items.get(4);
    }

    protected SsrBlockImpl slot5() {
        return items.get(5);
    }

    protected SsrBlockImpl slot6() {
        return items.get(6);
    }

    protected SsrBlockImpl slot7() {
        return items.get(7);
    }

    protected void dataRow(DataRow dataRow) {
        this.template.dataRow(dataRow);
    }

    @Override
    public SsrTemplate template() {
        return template;
    }

    public UISsrBoard template(SsrTemplate template) {
        this.template = template;
        return this;
    }

}
