package cn.cerc.ui.ssr.report;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.FastDate;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.EntityServiceField;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.source.ISupplierDataSet;
import cn.cerc.ui.ssr.source.ISupplierFields;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptGridCombined extends VuiContainer<AbstractRptGridControl> implements ISupportRptGrid {
    private PdfPCell header;
    private PdfPCell content;
    private int total;
    private HttpServletRequest request;

    @Column
    String title = "";
    @Column
    private int width;
    @Column
    RptCellAlign align = RptCellAlign.Center;
    @Column
    String summaryValue = "";
    @Column
    SummaryTypeEnum summaryType = SummaryTypeEnum.无;

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
                .setValue("class", RptGridIt.class.getSimpleName())
                .setValue("check", false);
        Optional<ISupplierDataSet> optSvr = Optional.empty();
        if (getOwner() instanceof RptGrid ownerGrid)
            optSvr = ownerGrid.dataSet.target();
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
                String classCode = RptGridString.class.getSimpleName();
                if (field.getType() == Boolean.class || field.getType() == boolean.class)
                    classCode = RptGridBoolean.class.getSimpleName();
                else if (field.getType() == Integer.class || field.getType() == int.class
                        || field.getType() == Double.class || field.getType() == double.class
                        || field.getType().isEnum())
                    classCode = RptGridNumeric.class.getSimpleName();
                else if (field.getType() == Datetime.class || field.getType() == FastDate.class)
                    classCode = RptGridDatetime.class.getSimpleName();
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
            if (item instanceof ISupportRptGrid gridField) {
                gridField.title(title);
                gridField.field(field);
            }
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.initPdfGridHeader:
            if (msgData instanceof PdfPCell header)
                this.header = header;
            break;
        case SsrMessage.initPdfGridContent:
            if (msgData instanceof PdfPCell content)
                this.content = content;
            break;
        case SsrMessage.InitDataIn:
            if (sender == getOwner() && msgData instanceof DataSet dataSet) {
                for (UIComponent item : this) {
                    if (item instanceof VuiComponent vui)
                        vui.onMessage(this, SsrMessage.InitDataIn, dataSet, null);
                }
            }
            break;
        }
    }

    @Override
    public Paragraph outputTotal(DataSet dataSet) {
        String value = switch (summaryType) {
        case 计数 -> String.valueOf(dataSet.size());
        case 固定 -> summaryValue;
        default -> "";
        };
        return new Paragraph(value, RptFontLibrary.f10());
    }

    @Override
    public SummaryTypeEnum summaryType() {
        return summaryType;
    }

    @Override
    public void output(HtmlWriter html) {
        if (total++ == 0) {
            header.setPhrase(new Paragraph(title, RptFontLibrary.f10()));
            return;
        }
        String collect = getComponents().stream().map(componet -> {
            if (componet instanceof AbstractRptGridControl control) {
                String text = control.content();
                if (Utils.isEmpty(text))
                    return null;
                return text;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.joining(","));
        content.setPhrase(new Paragraph(collect, RptFontLibrary.f10()));
        content.setHorizontalAlignment(align.cellAlign());
    }

    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public void field(String field) {
    }

    @Override
    public void width(int width) {
        this.width = width;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public RptCellAlign align() {
        return align;
    }

}
