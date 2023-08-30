package cn.cerc.ui.ssr.block;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierFields;
import cn.cerc.ui.ssr.source.VuiDataService;

public class VuiBoard extends VuiContainer<ISupportBlock> implements ISsrBoard {
//    private static final Logger log = LoggerFactory.getLogger(UISsrBoard.class);
    private static final int Max_slot = 8;
    private SsrBlock cpu;
    private List<SsrBlock> items = new ArrayList<>();
    private SsrTemplate template = new SsrTemplate("");
    private HttpServletRequest request;

    private Binder<VuiDataService> binder;
    private int index;

    public VuiBoard(UIComponent owner) {
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

    @Override
    public UIComponent addComponent(UIComponent child) {
        if (child instanceof ISupportBlock block) {
            updateSlot(block.request(this), index++);
        }
        return super.addComponent(child);
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorGrid grid = new EditorGrid(content, this);
        grid.build(pageCode);

        if (binder == null)
            return;

        Optional<VuiDataService> optSvr = binder.target();
        DataSet dataSet = new DataSet();
        UISsrBlock impl = new UISsrBlock(content,
                """
                        <form method="post" id="fieldForm">
                            <input type="hidden" name="id" value=${id}>
                            <div id="grid" class="scrollArea">
                                <table class="dbgrid">
                                    <tbody>
                                        <tr>
                                            <th style="width: 4em">选择</th>
                                            <th style="width: 10em">字段</th>
                                            <th style="width: 20em">类名</th>
                                        </tr>
                                        ${dataset.begin}
                                        <tr>
                                            <td align="center" role="check">
                                                <span><input type="checkbox" name="components" value="${class},${field},${title}"></span>
                                            </td>
                                            <td align="left" role="title">${title}</td>
                                            <td align="left" role="class">${class}</td>
                                        </tr>
                                        ${dataset.end}
                                    </tbody>
                                </table>
                            </div>
                            <div lowcode="button"><label onclick="selectItems('components')"><input type='checkbox' id='selectAll' />全选</label><button name="save" value="save" onclick="submitForm('fieldForm', 'submit')">保存</button>
                            </div>
                        </form>""");
        impl.block().dataSet(dataSet);
        impl.block().option("id", this.getId());

        dataSet.append()
                .setValue("field", "it")
                .setValue("title", "序")
                .setValue("class", BlockItFIeld.class.getSimpleName())
                .setValue("check", false);
        if (optSvr.isPresent()) {
            Set<Field> fields = optSvr.get().fields(ISupplierFields.BodyOutFields);
            for (Field field : fields) {
                if (dataSet.locate("field", field.getName()))
                    continue;
                String title = field.getName();
                Column column = field.getAnnotation(Column.class);
                if (column == null)
                    continue;
                if (!Utils.isEmpty(column.name()))
                    title = column.name();
                String classCode = BlockStringField.class.getSimpleName();
                if (field.getType() == Boolean.class || field.getType() == boolean.class)
                    classCode = BlockBooleanField.class.getSimpleName();
                else if (field.getType() == Integer.class || field.getType() == int.class
                        || field.getType() == Double.class || field.getType() == double.class
                        || field.getType().isEnum())
                    classCode = BlockNumberField.class.getSimpleName();
                dataSet.append()
                        .setValue("field", field.getName())
                        .setValue("title", title)
                        .setValue("class", classCode)
                        .setValue("check", false);
            }
        }
    }

    @Override
    public void saveEditor(RequestReader reader) {
        reader.saveProperties(this);
        // 对栏位进行排序saveEditor
        reader.sortComponent(this);
        // 批量添加组件
        batchAppendComponents();
        // 处理移除组件
        reader.removeComponent(this);
    }

    private void batchAppendComponents() {
        String[] components = request.getParameterValues("components");
        if (Utils.isEmpty(components))
            return;
        IVuiEnvironment environment = this.canvas().environment();
        for (String component : components) {
            String[] componentProperties = component.split(",");
            String clazz = componentProperties[0];
            String field = componentProperties[1];
            String title = componentProperties[2];
            Optional<VuiComponent> optBean = environment.getBean(clazz, VuiComponent.class);
            if (optBean.isEmpty())
                continue;
            VuiComponent item = optBean.get();
            item.setOwner(this);
            item.canvas(this.canvas());
            // 创建id
            String prefix = item.getIdPrefix();
            String nid = this.canvas().createUid(prefix);
            item.setId(nid);
            if (item instanceof ISupportBlock gridField) {
                gridField.title(title);
                gridField.field(field);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.InitBinder:
            if (sender == this.getOwner() && msgData instanceof Binder<?> binder) {
                this.binder = (Binder<VuiDataService>) binder;
            }
            break;
        }
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

    public VuiBoard cpu(SsrBlock cpu) {
        Objects.requireNonNull(cpu);
        this.cpu = cpu;
        for (int i = 0; i < Max_slot; i++)
            cpu.onCallback("slot" + i, () -> "");
        return this;
    }

    private VuiBoard updateSlot(SsrBlock slot, int index) {
        items.set(index, slot);
        slot.template(this.template);
        slot.id("slot" + index);
        return this;
    }

    protected VuiBoard slot0(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 0);
    }

    protected VuiBoard slot1(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 1);
    }

    protected VuiBoard slot2(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 2);
    }

    protected VuiBoard slot3(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 3);
    }

    protected VuiBoard slot4(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 4);
    }

    protected VuiBoard slot5(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 5);
    }

    protected VuiBoard slot6(ISupplierBlock slot) {
        return updateSlot(slot.request(this), 6);
    }

    protected VuiBoard slot7(ISupplierBlock slot) {
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

    public VuiBoard template(SsrTemplate template) {
        this.template = template;
        return this;
    }

}
