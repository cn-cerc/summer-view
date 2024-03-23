package cn.cerc.ui.ssr.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.EntityServiceField;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.ISupplierFields;

@Component
@Description("分组面板")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiTabSheet extends VuiContainer<ISupportField> implements ISupportForm {
    private HttpServletRequest request;
    @Column(name = "分组名称")
    String name = "";
    @Column
    Binder<VuiTabControl> binder = new Binder<>(this, VuiTabControl.class);

    public VuiTabSheet() {
        super();
    }

    @Override
    public Set<Class<? extends VuiComponent>> getChildren() {
        return VuiContainer.getChildren(this, ISupportField.class);
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);
        VuiForm form = this.findOwner(VuiForm.class);
        if (form != null) {
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

            List<EntityServiceField> fields = new ArrayList<>();
            Optional<ISupplierDataRow> optDataRow = form.dataRow.target();
            if (optDataRow.isPresent() && optDataRow.get() instanceof ISupplierFields supperli)
                fields.addAll(supperli.fields(ISupplierFields.BodyOutFields));

            Optional<ISupplierFields> optSvr = form.binders().findOwner(ISupplierFields.class);
            if (optSvr.isPresent())
                fields.addAll(optSvr.get().fields(ISupplierFields.HeadInFields));
            for (EntityServiceField field : fields) {
                if (dataSet.locate("field", field.getName()))
                    continue;
                String title = field.getName();
                Column column = field.getAnnotation(Column.class);
                if (column == null)
                    continue;
                if (!Utils.isEmpty(column.name()))
                    title = column.name();
                String classCode = FormStringField.class.getSimpleName();
                if (field.getType() == Boolean.class || field.getType() == boolean.class)
                    classCode = FormBooleanField.class.getSimpleName();
                else if (field.getType() == Integer.class || field.getType() == int.class
                        || field.getType() == Double.class || field.getType() == double.class
                        || field.getType().isEnum())
                    classCode = FormNumberField.class.getSimpleName();
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
        // 对栏位进行排序
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
            if (item instanceof ISupportField formField) {
                formField.title(title);
                formField.field(field);
            }
            VuiForm form = this.findOwner(VuiForm.class);
            if (form != null)
                this.canvas().sendMessage(form, SsrMessage.appendComponent, item, form.dataRow.targetId());
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            // 如果name有设置值且找得到VuiForm的父辈元素，则默认为查询模式
            if (!Utils.isEmpty(name)) {
                VuiForm form = this.findOwner(VuiForm.class);
                if (form != null)
                    form.setRole("vuiForm");
            }
            break;
        case SsrMessage.InitBinder:
            this.binder.init();
            break;
        }

    }

    @Override
    public void output(HtmlWriter html) {
        Optional<VuiTabControl> target = this.binder.target();
        if (target.isPresent())
            return;
        if (Utils.isEmpty(name)) {
            this.setRootLabel("ul");
            super.output(html);
        } else {
            html.println("<div class='sheet'>");
            html.print("<div>%s</div>", name);
            html.println("<ul>");
            super.output(html);
            html.println("</ul>");
            html.println("</div>");
        }
    }

}
