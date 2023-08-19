package cn.cerc.ui.ssr.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class UISsrBoard extends UIComponent implements ISsrBoard {
//    private static final Logger log = LoggerFactory.getLogger(UISsrBoard.class);
    private static final int Max_slot = 8;
    private SsrBlock cpu;
    private List<SsrBlock> items = new ArrayList<>();
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

        for (SsrBlock slot : items) {
            slot.template(template);
            cpu.onCallback(slot.id(), () -> slot.html());
        }

        cpu.template(template);
        html.print(cpu.html());
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

    public UISsrBoard cpu(SsrBlock cpu) {
        Objects.requireNonNull(cpu);
        this.cpu = cpu;
        for (int i = 0; i < Max_slot; i++)
            cpu.onCallback("slot" + i, () -> "");
        return this;
    }

    private UISsrBoard updateSlot(SsrBlock slot, int index) {
        items.set(index, slot);
        slot.template(this.template);
        slot.id("slot" + index);
        return this;
    }

    protected UISsrBoard slot0(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 0);
    }

    protected UISsrBoard slot1(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 1);
    }

    protected UISsrBoard slot2(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 2);
    }

    protected UISsrBoard slot3(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 3);
    }

    protected UISsrBoard slot4(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 4);
    }

    protected UISsrBoard slot5(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 5);
    }

    protected UISsrBoard slot6(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 6);
    }

    protected UISsrBoard slot7(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 7);
    }

    protected SsrBlock slot0() {
        return items.get(0);
    }

    protected SsrBlock slot1() {
        return items.get(1);
    }

    protected SsrBlock slot2() {
        return items.get(2);
    }

    protected SsrBlock slot3() {
        return items.get(3);
    }

    protected SsrBlock slot4() {
        return items.get(4);
    }

    protected SsrBlock slot5() {
        return items.get(5);
    }

    protected SsrBlock slot6() {
        return items.get(6);
    }

    protected SsrBlock slot7() {
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
