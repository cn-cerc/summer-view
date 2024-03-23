package cn.cerc.ui.ssr.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

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
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataSet;
import cn.cerc.ui.ssr.source.ISupplierFields;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptGrid extends VuiContainer<ISupportRptGrid> implements ISupportRpt {
    private static final Logger log = LoggerFactory.getLogger(RptGrid.class);
    private HttpServletRequest request;
    private Document document;

    @Column(name = "是否显示边框")
    boolean showBorder = true;
    @Column(name = "正文表格高度")
    int bodyCellMinimumHeight = 20;
    @Column
    Binder<ISupplierDataSet> dataSet = new Binder<>(this, ISupplierDataSet.class);

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.InitPdfDocument:
            if (msgData instanceof Document document)
                this.document = document;
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
    public void output(HtmlWriter html) {
        try {
            // 创建一个N列的表格控件
            PdfPTable table = new PdfPTable(getComponentCount());
            // 设置表格占PDF文档100%宽度
            table.setWidthPercentage(100);
            // 水平方向表格控件左对齐
            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            // 创建一个表格的表头单元格
            PdfPCell headerCell = new PdfPCell();
            // 设置表格的表头单元格颜色
            headerCell.setUseAscender(true);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 垂直居中
            headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            headerCell.setMinimumHeight(bodyCellMinimumHeight);
            if (!this.showBorder) {
                headerCell.setBorder(Rectangle.NO_BORDER);
            }
            // 输出标题
            for (UIComponent item : this) {
                if (item instanceof VuiComponent vui)
                    vui.onMessage(this, SsrMessage.initPdfGridHeader, headerCell, null);
                item.output(html);
                table.addCell(headerCell);
            }

            if (this.dataSet.target().isEmpty())
                return;
            DataSet dataSet = this.dataSet.target().get().dataSet();
            if (dataSet == null)
                return;

            for (UIComponent item : this) {
                if (item instanceof VuiComponent vui)
                    vui.onMessage(this, SsrMessage.InitDataIn, dataSet, null);
            }

            // 创建一个表格的正文内容单元格
            PdfPCell contentCell = new PdfPCell();
            contentCell.setUseAscender(true);
            contentCell.setMinimumHeight(bodyCellMinimumHeight);
            contentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            contentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            if (!this.showBorder) {
                contentCell.setBorder(Rectangle.NO_BORDER);
            }
            // 输出表格内容
            dataSet.first();
            while (dataSet.fetch()) {
                for (UIComponent item : this) {
                    if (item instanceof VuiComponent vui)
                        vui.onMessage(this, SsrMessage.initPdfGridContent, contentCell, null);
                    item.output(html);
                    table.addCell(contentCell);
                }
            }

            // 按百分比设置表格的列宽度
            List<Integer> columnWidth = new ArrayList<>();
            for (UIComponent item : this) {
                if (item instanceof ISupportRptGrid vui) {
                    columnWidth.add(vui.width());
                }
            }
            int widthSum = columnWidth.stream().mapToInt(t -> t).sum();
            if (widthSum > 0) {
                float ratio = 100f / widthSum;
                float[] relativeWidths = new float[columnWidth.size()];
                for (int i = 0; i < columnWidth.size(); i++) {
                    relativeWidths[i] = columnWidth.get(i) * ratio;
                }
                table.setWidths(relativeWidths);
            }

            boolean existSummary = this.getComponents().stream().anyMatch(component -> {
                if (component instanceof ISupportRptGrid item)
                    return item.summaryType() != SummaryTypeEnum.无;
                return false;
            });
            if (existSummary) {
                for (UIComponent component : this) {
                    if (component instanceof ISupportRptGrid item) {
                        contentCell.setPhrase(item.outputTotal(dataSet));
                        contentCell.setHorizontalAlignment(item.align().cellAlign());
                    }
                    table.addCell(contentCell);
                }
            }
            document.add(table);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }

}
