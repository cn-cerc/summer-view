package cn.cerc.ui.ssr.excel;

import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.Describe;
import cn.cerc.db.core.FastDate;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.EntityServiceField;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataSet;
import cn.cerc.ui.ssr.source.ISupplierFields;
import cn.cerc.ui.ssr.source.VuiDataService;
import jxl.write.WritableSheet;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsGrid extends VuiContainer<ISupportXlsGrid> implements ISupportXls {
    @Column
    Binder<ISupplierDataSet> dataSet = new Binder<>(this, ISupplierDataSet.class);

    private HttpServletRequest request;
    private WritableSheet sheet;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.InitSheet:
            if (msgData instanceof WritableSheet sheet)
                this.sheet = sheet;
            break;
        }
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorGrid grid = new EditorGrid(content, this);
        grid.build(pageCode);

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
                                                <span><input type="checkbox" name="components" value="${class},${field},${width},${title}"></span>
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

        Optional<ISupplierDataSet> optSvr = this.dataSet.target();
        if (optSvr.isPresent() && optSvr.get() instanceof VuiDataService svr) {
            List<EntityServiceField> fields = svr.fields(ISupplierFields.BodyOutFields);
            for (EntityServiceField field : fields) {
                if (dataSet.locate("field", field.getName()))
                    continue;
                String title = field.getName();
                Column column = field.getAnnotation(Column.class);
                if (column == null)
                    continue;
                if (!Utils.isEmpty(column.name()))
                    title = column.name();
                int width = 10;
                Describe describe = field.getAnnotation(Describe.class);
                if (describe != null && describe.width() > 0)
                    width = describe.width();
                String classCode = XlsStringColumn.class.getSimpleName();
                if (field.getType() == Boolean.class || field.getType() == boolean.class)
                    classCode = XlsBooleanColumn.class.getSimpleName();
                else if (field.getType() == Integer.class || field.getType() == int.class
                        || field.getType() == Double.class || field.getType() == double.class
                        || field.getType().isEnum())
                    classCode = XlsNumberColumn.class.getSimpleName();
                else if (field.getType() == Datetime.class || field.getType() == FastDate.class)
                    classCode = XlsDatetimeColumn.class.getSimpleName();
                dataSet.append()
                        .setValue("field", field.getName())
                        .setValue("title", title)
                        .setValue("class", classCode)
                        .setValue("width", width)
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
            int width = Utils.strToIntDef(componentProperties[2], 10);
            String title = componentProperties[3];
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
            if (item instanceof ISupportXlsGrid gridField) {
                gridField.title(title);
                gridField.field(field);
                gridField.width(width);
            }
            this.canvas().sendMessage(this, SsrMessage.appendComponent, item, this.dataSet.targetId());
        }
    }

    @Override
    public void output(HtmlWriter html) {
        for (var item : this) {
            item.output(html);
        }
        this.canvas().sendMessage(this, SsrMessage.SheetNextRow, 1, null);
        if (this.dataSet.target().isEmpty())
            return;
        var dataSet = this.dataSet.target().get().dataSet();
        if (dataSet != null) {
            dataSet.first();
            while (dataSet.fetch()) {
                if (sheet.getRows() > 65535)
                    throw new RuntimeException("你导出的数据量过大，超过了Excel的上限，请调整查询条件");
                for (var item : this) {
                    item.output(html);
                }
                this.canvas().sendMessage(this, SsrMessage.SheetNextRow, 1, null);
            }
        }
    }

    public DataSet dataSet() {
        if (dataSet.target().isPresent())
            return dataSet.target().get().dataSet();
        else
            return null;
    }

}
