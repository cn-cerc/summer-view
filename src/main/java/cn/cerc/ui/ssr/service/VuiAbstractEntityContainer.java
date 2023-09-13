package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

public abstract class VuiAbstractEntityContainer<T extends ISupportServiceField> extends VuiContainer<T>
        implements ISupplierEntityFields {

    private HttpServletRequest request;

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorGrid grid = new EditorGrid(content, this);
        grid.addColumn("栏位", "cloumn", 20);
        grid.build(pageCode);

        // 显示所有可以加入的组件
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
                                            <th style="width: 10em">必填</th>
                                        </tr>
                                        ${dataset.begin}
                                        <tr>
                                            <td align="center" role="check">
                                                <span><input type="checkbox" name="components" value="${field},${title}"></span>
                                            </td>
                                            <td align="left" role="title">${title}</td>
                                            <td align="left" role="required">${if required}是${else}否${endif}</td>
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

        Set<Field> fields = entityFields();
        for (Field field : fields) {
            if (dataSet.locate("field", field.getName()))
                continue;
            String title = field.getName();
            Column column = field.getAnnotation(Column.class);
            if (column == null)
                continue;
            if (!Utils.isEmpty(column.name()))
                title = column.name();
            boolean required = !column.nullable();
            dataSet.append()
                    .setValue("field", field.getName())
                    .setValue("title", title)
                    .setValue("required", required ? "1" : "")
                    .setValue("check", false);
        }
    }

    @Override
    public void saveEditor(RequestReader reader) {
        super.saveEditor(reader);

        String[] components = request.getParameterValues("components");
        if (Utils.isEmpty(components))
            return;
        Set<Class<? extends VuiComponent>> children = getChildren();
        if (Utils.isEmpty(children))
            return;
        Class<?> clazz = children.stream().findFirst().get();
        IVuiEnvironment environment = this.canvas().environment();
        for (String component : components) {
            String[] componentProperties = component.split(",");
            String field = componentProperties[0];
            String title = componentProperties[1];
            Optional<VuiComponent> optBean = environment.getBean(clazz.getSimpleName(), VuiComponent.class);
            if (optBean.isEmpty())
                continue;
            VuiComponent item = optBean.get();
            item.setOwner(this);
            item.canvas(this.canvas());
            // 创建id
            String prefix = item.getIdPrefix();
            String nid = this.canvas().createUid(prefix);
            item.setId(nid);
            if (item instanceof ISupportServiceField serviceField) {
                serviceField.title(title);
                serviceField.field(field);
            }
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        }
    }

    protected void sendMessageToChild(int msgType, Object msgData) {
        for (UIComponent component : this.getComponents()) {
            if (component instanceof VuiComponent vuiComponent)
                vuiComponent.onMessage(this, msgType, msgData, null);
        }
    }

}
