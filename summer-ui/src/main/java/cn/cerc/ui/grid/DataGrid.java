package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.core.ClassResource;
import cn.cerc.core.DataRow;
import cn.cerc.core.DataSet;
import cn.cerc.core.DataSource;
import cn.cerc.core.Utils;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.grid.lines.AbstractGridLine;
import cn.cerc.ui.grid.lines.ChildGridLine;
import cn.cerc.ui.grid.lines.ExpenderGridLine;
import cn.cerc.ui.grid.lines.MasterGridLine;
import cn.cerc.ui.vcl.UIForm;

public class DataGrid extends UIComponent implements DataSource {
    private static final Logger log = LoggerFactory.getLogger(DataGrid.class);
    private static final ClassResource res = new ClassResource(DataGrid.class, SummerUI.ID);
    private static final double MaxWidth = 600;
    // 行管理器, 其中第1个一定为masterLine
    private List<AbstractGridLine> lines = new ArrayList<>();
    // 扩展行
    private ExpenderGridLine expender;
    // 手机行
    protected List<PhoneLine> phoneLines = new ArrayList<>();
    // 表单，后不得再使用
    private UIForm uiform;
    // 数据源
    private DataSet dataSet;
    // 支持表格分页
    private MutiPage pages = new MutiPage();
    private IForm form;
    // 以下参数为WEB模式下时专用
    private String gridCssClass = "dbgrid";
    private String gridCssStyle;
    // 输出每列时的事件
    private OutputEvent beforeOutput;

    public DataGrid(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
        this.setCssClass("scrollArea");
        this.setId("grid");
        lines.add(new MasterGridLine(this));
        if (this.getOrigin() instanceof IForm) {
            this.form = (IForm) this.getOrigin();
            pages.setRequest(this.form.getRequest());
        }
    }

    @Deprecated
    public DataGrid(IForm form, UIComponent owner) {
        this(owner);
        this.form = form;
        pages.setRequest(this.form.getRequest());
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public DataGrid setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        pages.setDataSet(dataSet);
        return this;
    }

    @Deprecated
    public void addField(AbstractField field) {
        this.addComponent(field);
    }

    @Override
    public UIComponent addComponent(UIComponent child) {
        if (child instanceof AbstractField) {
            child.setOwner(getMasterLine());
        } else {
            super.addComponent(child);
        }
        return this;
    }

    public MutiPage getPages() {
        return pages;
    }

    public List<AbstractField> getFields() {
        List<AbstractField> items = new ArrayList<>();
        for (AbstractField obj : lines.get(0).getFields()) {
            if (obj instanceof AbstractField) {
                items.add(obj);
            }
        }
        return items;
    }

    @Deprecated
    public UIForm getForm() {
        return uiform;
    }

    @Deprecated
    public void setForm(UIForm form) {
        this.uiform = form;
    }

    public final UIComponent getExpender() {
        if (form.getClient().isPhone()) {
            return this;
        } else {
            if (expender == null) {
                expender = new ExpenderGridLine(this);
                this.getLines().add(expender);
            }
            return expender;
        }
    }

    public List<AbstractGridLine> getLines() {
        return lines;
    }

    public AbstractGridLine getLine(int index) {
        if (index == lines.size()) {
            lines.add(new ChildGridLine(this));
        }
        return lines.get(index);
    }

    public final MasterGridLine getMasterLine() {
        return (MasterGridLine) lines.get(0);
    }

    @Override
    public final boolean isReadonly() {
        return true;
    }

    public final String getCSSClass() {
        return gridCssClass;
    }

    public final void setCSSClass(String CSSClass) {
        this.gridCssClass = CSSClass;
    }

    @Override
    public final void output(HtmlWriter html) {
        if(this.isClientRender()) {
            html.println("let grid = new sci.TGrid(app)");
            html.println("grid.setDataSet(new sci.DataSet('%s'))", this.dataSet.toJson());
            return;
        }
        
        this.beginOutput(html);
        if (!this.isPhone() || this.getDataSet().size() > 0) {
            if (getForm() != null)
                getForm().beginOutput(html);
            if (this.isPhone())
                this.outputPhoneGrid(html);
            else
                this.outputWebGrid(html);
            if (getForm() != null)
                getForm().endOutput(html);
        }
        this.endOutput(html);
    }

    private void outputWebGrid(HtmlWriter html) {
        double sumFieldWidth = 0;
        for (RowCell cell : this.getMasterLine().getOutputCells())
            sumFieldWidth += cell.getFields().get(0).getWidth();
        if (sumFieldWidth < 0)
            throw new RuntimeException(res.getString(1, "总列宽不允许小于1"));
        if (sumFieldWidth > MaxWidth)
            throw new RuntimeException(String.format(res.getString(2, "总列宽不允许大于%s"), MaxWidth));

        html.print("<table class=\"%s\"", this.gridCssClass);
        if (this.gridCssStyle != null) {
            html.print(" style=\"%s\"", this.gridCssStyle);
        }
        html.println(">");

        html.println("<tr>");
        for (RowCell cell : this.getMasterLine().getOutputCells()) {
            AbstractField field = cell.getFields().get(0);
            html.print("<th");
            if (field.getWidth() == 0) {
                html.print(" style=\"display:none\"");
            } else {
                double val = Utils.roundTo(field.getWidth() / sumFieldWidth * 100, -2);
                html.print(" width=\"%f%%\"", val);
            }

            html.print("onclick=\"gridSort(this,'%s')\"", field.getField());
            html.print(">");
            html.print(field.getName());
            html.println("</th>");
        }
        html.println("</tr>");
        if (this.dataSet.size() > 0) {
            int i = this.pages.getBegin();
            while (i <= this.pages.getEnd()) {
                this.dataSet.setRecNo(i + 1);
                for (int lineNo = 0; lineNo < this.getLines().size(); lineNo++) {
                    AbstractGridLine line = this.getLine(lineNo);
                    if (line instanceof ExpenderGridLine) {
                        line.getCell(0).setColSpan(this.getMasterLine().getFields().size());
                    }
                    if (line instanceof ChildGridLine && this.beforeOutput != null) {
                        beforeOutput.process(line);
                    }
                    line.output(html, lineNo);
                }
                // 下一行
                i++;
            }
        }
        html.println("</table>");
    }

    private void outputPhoneGrid(HtmlWriter html) {
        if (this.dataSet.size() == 0) 
            return;

        html.println(String.format("<ol class=\"%s\">", "context"));

        int i = this.pages.getBegin();
        while (i <= this.pages.getEnd()) {
            this.dataSet.setRecNo(i + 1);
            int flag = 0;
            html.println("<li>");
            for (PhoneLine line : this.phoneLines) {
                if (line.isTable()) {
                    if (flag == 0) {
                        html.println("<table>");
                        flag = 1;
                    } else if (flag == 2) {
                        html.println("</table>");
                        html.println("<table>");
                    }
                } else {
                    if (flag == 1) {
                        html.println("</table>");
                        flag = 2;
                    }
                }
                line.output(html);
            }
            if (flag == 1) {
                html.println("</table>");
            }
            html.println("</li>");
            i++;
        }
        html.println("</ol>");
        return;
    }

    public String getCSSStyle() {
        return gridCssStyle;
    }

    public void setCSSStyle(String cSSStyle) {
        if (form.getClient().isPhone())
            log.info("only support web device");
        gridCssStyle = cSSStyle;
    }

    public OutputEvent getBeforeOutput() {
        if (form.getClient().isPhone())
            log.info("only support web device");
        return beforeOutput;
    }

    public void setBeforeOutput(OutputEvent beforeOutput) {
        if (form.getClient().isPhone())
            log.info("only support web device");
        this.beforeOutput = beforeOutput;
    }

    public String getPrimaryKey() {
        if (form.getClient().isPhone())
            return null;
        else
            return getMasterLine().getPrimaryKey();
    }

    public DataGrid setPrimaryKey(String primaryKey) {
        if (form.getClient().isPhone()) {
            log.info("only support web device");
        } else {
            this.getMasterLine().setPrimaryKey(primaryKey);
        }
        return this;
    }

    public PhoneLine addLine() {
        if (!form.getClient().isPhone())
            log.info("only support phone device");
        PhoneLine line = new PhoneLine(this);
        phoneLines.add(line);
        return line;
    }

    @Override
    public DataRow getCurrent() {
        return this.dataSet.getCurrent();
    }

}
