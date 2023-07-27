
package cn.cerc.ui.style;

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

    private String dateDialogIcon;
    private String fieldDialogIcon;

    public SsrFormStyleDefault() {
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null) {
            this.dateDialogIcon = impl.getClassProperty(DateField.class, SummerUI.ID, "icon", "");
            this.fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
        } else {
            this.dateDialogIcon = DateConfig.getClassProperty("icon", "");
            this.fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
        }
    }

    @Override
    public SupplierTemplateImpl getSubmitButton() {
        return form -> {
            var title = UISsrForm.FormBegin;
            var ssr = form.addTemplate(title,
                    """
                            <form method='post' action='${action}' role='${role}'>
                                <div>
                                    <span onclick="toggleSearch(this)">查询条件</span>
                                    <div class="searchFormButtonDiv">
                                        <button name="submit" value="search">查询</button>
                                        <button role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">配置</button>
                                    </div>
                                </div>
                                <ul>
                            """);
            ssr.setOption("action", "").setOption("role", "search");
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getTabs(String field) {
        return form -> {
            var title = UISsrForm.FormBegin;
            var ssr = form.addTemplate(title,
                    String.format(
                            """
                                        <form method='post' action='${action}' role='${role}'>
                                        <div>
                                            <nav>
                                            <input type="hidden" name="%s" value="${%s}">
                                            ${map.begin}
                                            <span ${if map.key==%s}class="checked"${endif} onclick="updateTab(this, ${map.key})">${map.value}</span>
                                            ${map.end}
                                            </nav>
                                            <div class="searchFormButtonDiv">
                                                <button name="submit" value="search">查询</button>
                                                <button role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">配置</button>
                                            </div>
                                        </div>
                                        <ul>
                                    """,
                            field, field, field));
            ssr.setOption("action", "").setOption("role", "search");
            ssr.setOption("fields", field);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getString(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title,
                    String.format(
                            """
                                    <li>
                                        <label for="%s"><em>%s</em></label>
                                        <div>
                                            <input autocomplete="off" name="%s" id="%s" type="text" value="${%s}" ${if readonly}readonly${endif}
                                            ${if autofocus}autofocus${endif} ${if placeholder}placeholder="${placeholder}"${endif}
                                            ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif} />
                                            <span role="suffix-icon"></span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field));
            ssr.setOption("readonly", "false");
            ssr.setOption("autofocus", "false");
            ssr.setOption("placeholder", "false");
            ssr.setOption("pattern", "false");
            ssr.setOption("required", "false");
            ssr.setOption("fields", field).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getString(String title, String field, String... dialogFunc) {
        items.add(title);
        return form -> {
            var dialogText = getDialogText(field, dialogFunc);
            var ssr = form.addTemplate(title,
                    String.format(
                            """
                                    <li>
                                        <label for="%s"><em>%s</em></label>
                                        <div>
                                            <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off" placeholder="请点击获取%s" ${if readonly}readonly${endif} />
                                            <span role="suffix-icon">
                                                <a href="javascript:%s">
                                                    <img src="${dialogIcon}">
                                                </a>
                                            </span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field, title, dialogText));
            ssr.setOption("dialogIcon", fieldDialogIcon);
            ssr.setOption("readonly", "false");
            ssr.setOption("fields", field).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getBoolean(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title,
                    String.format(
                            """
                                    <li>
                                        <div role="switch">
                                            <input autocomplete="off" name="%s" id="%s" type="checkbox" value="1" ${if %s}checked ${endif}/>
                                        </div>
                                        <label for="%s"><em>%s</em></label>
                                    </li>
                                            """,
                            field, field, field, field, title));
            ssr.setOption("fields", field).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getMap(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title, String.format("""
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
            ssr.setOption("fields", field).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getCodeName(String title, String field, String... dialogFunc) {
        items.add(title);
        return form -> {
            var dialogText = getDialogText(String.format("%s,%s_name", field, field), dialogFunc);
            var ssr = form.addTemplate(title,
                    String.format(
                            """
                                    <li>
                                        <label for="%s_name"><em>%s</em></label>
                                        <div>
                                            <input type="hidden" name="%s" id="%s" value="${%s}">
                                            <input type="text" name="%s_name" id="%s_name" value="${%s_name}" autocomplete="off" placeholder="请点击获取%s" 
                                            ${if readonly}readonly${endif} >
                                            <span role="suffix-icon">
                                                <a href="javascript:%s">
                                                    <img src="${dialogIcon}">
                                                </a>
                                            </span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field, field, field, field, title, dialogText));
            ssr.setOption("dialogIcon", fieldDialogIcon);
            ssr.setOption("readonly", "true");
            ssr.setOption("fields", String.format("%s,%s_name", field, field)).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    protected String getDialogText(String field, String... dialogFunc) {
        var sb = new StringBuffer();
        sb.append(dialogFunc[0]);
        sb.append("('").append(field).append("'");
        if (dialogFunc.length > 1) {
            for (var i = 1; i < dialogFunc.length; i++)
                sb.append(",'").append(dialogFunc[i]).append("'");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public SupplierTemplateImpl getDate(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title, String.format("""
                    <li>
                        <label for="%s"><em>%s</em></label>
                        <div>
                            <input autocomplete="off" name="%s" id="%s" type="text" value="${%s}"
                            ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif} />
                            <span role="suffix-icon">
                                <a href="javascript:showDateDialog('%s')">
                                    <img src="${dialogIcon}" />
                                </a>
                            </span>
                        </div>
                    </li>
                    """, field, title, field, field, field, field));
            ssr.setOption("dialogIcon", dateDialogIcon);
            ssr.setOption("pattern", "false");
            ssr.setOption("required", "false");
            ssr.setOption("fields", field).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getDatetime(String title, String field) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title, String.format("""
                    <li>
                        <label for="%s"><em>%s</em></label>
                        <div>
                            <input autocomplete="off" name="%s" id="%s" type="text" value="${%s}" />
                            <span role="suffix-icon">
                                <a href="javascript:showDateTimeDialog('%s')">
                                    <img src="${dialogIcon}">
                                </a>
                            </span>
                        </div>
                    </li>
                    """, field, title, field, field, field, field));
            ssr.setOption("fields", field);
            ssr.setOption("dialogIcon", dateDialogIcon).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getDateRange(String title, String beginDate, String endDate) {
        items.add(title);
        return form -> {
            var ssr = form.addTemplate(title, String.format("""
                    <li>
                        <label for="start_date_"><em>%s</em></label>
                        <div class="dateArea">
                            <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                            ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif} />
                            <span>/</span>
                            <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                            ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif} />
                            <span role="suffix-icon">
                                <a href="javascript:showDateAreaDialog('%s', '%s')">
                                <img src="${dialogIcon}" />
                                </a>
                            </span>
                        </div>
                    </li>
                    """, title, beginDate, beginDate, beginDate, endDate, endDate, endDate, beginDate, endDate));

            ssr.setOption("dialogIcon", dateDialogIcon);
            ssr.setOption("pattern", "false");
            ssr.setOption("required", "false");
            ssr.setOption("fields", String.format("%s,%s", beginDate, endDate)).setOption("option", "1");
            ssr.setId(title);
            return ssr;
        };
    }

    @Override
    public List<String> items() {
        return items;
    }

}
