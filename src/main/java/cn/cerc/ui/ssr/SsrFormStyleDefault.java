
package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;

public class SsrFormStyleDefault implements SsrFormStyleImpl {
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);
    private static final ClassConfig DateConfig = new ClassConfig(DateField.class, SummerUI.ID);

    private List<String> items = new ArrayList<>();

    private String fieldDialogIcon;
    private String dateDialogIcon;

    public SsrFormStyleDefault() {
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null) {
            this.fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
            this.dateDialogIcon = impl.getClassProperty(DateField.class, SummerUI.ID, "icon", "");
        } else {
            this.fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
            this.dateDialogIcon = DateConfig.getClassProperty("icon", "");
        }

    }

    @Override
    public SupplierBlockImpl getSubmitButton() {
        return form -> {
            var ssr = form.addBlock(UISsrForm.FormStart,
                    """
                               <div>
                                    <span onclick="toggleSearch(this)">查询条件</span>
                                    <div class="searchFormButtonDiv">
                                        <button name="submit" value="search">查询</button>${if templateId}
                                        <button role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">配置</button>${endif}
                                    </div>
                                </div>
                            """);
            ssr.option("action", "").option("role", "search").option("templateId", "");
            return ssr;
        };
    }

    @Override
    public SupplierField getString(String title, String field) {
        items.add(title);
        return new SupplierField(title, field);
    }

    @Override
    public SupplierBlockImpl getBoolean(String title, String field) {
        items.add(title);
        return new SupplierBooleanField(title, field);
    }

    @Override
    public SupplierBlockImpl getMap(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addBlock(title, String.format("""
                    <li>
                        <label for="%s"><em>%s</em></label>
                        <div>
                            <select id="%s" name="%s">
                            ${map.begin}
                                <option value="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                            ${map.end}
                            </select>
                        </div>
                    </li>
                    """, field, title, field, field, field));
            ssr.fields(field).display(1);
            ssr.id(title);
            return ssr;
        };
    }

    @Override
    public SupplierCodeNameField getCodeName(String title, String field, String... dialogFunc) {
        items.add(title);
        var ssr = new SupplierCodeNameField(title, field, dialogFunc);
        ssr.options("dialogIcon", fieldDialogIcon);
        return ssr;
    }

    protected String getDialogText(String field, String... dialogFunc) {
        return SupplierField.getDialogText(field, dialogFunc);
    }

    @Override
    public SupplierField getDate(String title, String field) {
        items.add(title);
        var ssr = getString(title, field);
        ssr.dialog("showDateDialog").options("dialogIcon", dateDialogIcon);
        return ssr;
    }

    @Override
    public SupplierBlockImpl getDatetime(String title, String field) {
        items.add(title);
        var ssr = getString(title, field);
        ssr.dialog("showDateTimeDialog").options("dialogIcon", dateDialogIcon);
        return ssr;
    }

    @Override
    public SupplierDateRangeField getDateRange(String title, String beginDate, String endDate) {
        items.add(title);
        var ssr = new SupplierDateRangeField(title, beginDate, endDate);
        ssr.options("dialogIcon", dateDialogIcon);
        return ssr;
    }

    @Override
    public List<String> items() {
        return items;
    }

}
