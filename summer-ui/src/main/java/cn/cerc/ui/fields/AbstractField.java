package cn.cerc.ui.fields;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.core.ClassConfig;
import cn.cerc.core.DataSource;
import cn.cerc.core.Datetime;
import cn.cerc.core.FastDate;
import cn.cerc.core.Record;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.SearchSource;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.other.BuildText;
import cn.cerc.ui.other.BuildUrl;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UILabel;
import cn.cerc.ui.vcl.UILi;
import cn.cerc.ui.vcl.UISpan;
import cn.cerc.ui.vcl.UIText;
import cn.cerc.ui.vcl.UITextarea;
import cn.cerc.ui.vcl.UIUrl;

public abstract class AbstractField extends UIComponent implements INameOwner, SearchSource {
    private static final ClassConfig config = new ClassConfig(AbstractField.class, SummerUI.ID);
    // 数据库相关
    private String field;
    // 自定义取值
    protected BuildText buildText;
    // 焦点否
    protected boolean autofocus;
    //
    protected boolean required;
    // 用于文件上传是否可以选则多个文件
    protected boolean multiple = false;
    //
    protected String placeholder;
    // 正则过滤
    protected String pattern;
    //
    protected boolean hidden;
    // 角色
    protected String role;
    //
    protected DialogField dialog;
    // dialog 小图标
    protected String icon;
    //
    protected BuildUrl buildUrl;
    // 数据源
    private DataSource source;

    protected String oninput;
    protected String onclick;
    private String htmlTag = "input";
    private String htmType = UIInput.TYPE_TEXT;
    private String name;
    private String shortName;
    private String align;
    private int width;
    // 手机专用样式
    private String CSSClass_phone;
    // value
    private String value;
    // 只读否
    private int readonly = -1;
    // 自动完成（默认为 off）
    private boolean autocomplete = false;
    // 栏位说明
    private UIText mark;
    private boolean visible = true;
    // TODO 专用于textarea标签，需要拆分该标签出来，黄荣君 2016-05-31
    // 最大字符串数
    private int maxlength;
    // 可见行数
    private int rows;
    // 可见宽度
    private int cols;
    // 是否禁用
    private boolean resize = true;
    // 是否显示*号
    private boolean showStar = false;

    public AbstractField(UIComponent owner, String name, String field) {
        this(owner, name, field, 0);
    }

    public AbstractField(UIComponent owner, String name, String field, int width) {
        super(owner);
        this.setField(field);
        this.setName(name);
        this.width = width;
        // 查找最近的数据源
        UIComponent root = owner;
        while (root != null) {
            if (root instanceof DataSource) {
                this.source = (DataSource) root;
                break;
            }
            root = root.getOwner();
        }
    }

    public UIText getMark() {
        return mark;
    }

    public AbstractField setMark(UIText mark) {
        this.mark = mark;
        return this;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getWidth() {
        return width;
    }

    public AbstractField setWidth(int width) {
        this.width = width;
        return this;
    }

    public String getShortName() {
        if (this.shortName != null) {
            return this.shortName;
        }
        return this.getName();
    }

    public AbstractField setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getHtmType() {
        return htmType;
    }

    public AbstractField setHtmType(String htmType) {
        this.htmType = htmType;
        return this;
    }

    public String getAlign() {
        return align;
    }

    public AbstractField setAlign(String align) {
        this.align = align;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public AbstractField setName(String name) {
        this.name = name;
        return this;
    }

    public String getField() {
        return field;
    }

    public AbstractField setField(String field) {
        this.field = field;
        if (this.getId() == null || this.getId().startsWith("component")) {
            this.setId(field);
            return this;
        } else {
            return this;
        }
    }

    public String getText() {
        return getDefaultText();
    }

    /**
     * @return 返回输出文本
     */
    protected String getDefaultText() {
        Record record = this.getCurrent();
        if (record != null) {
            if (buildText != null) {
                HtmlWriter html = new HtmlWriter();
                buildText.outputText(record, html);
                return html.toString();
            }
            return record.getString(getField());
        } else {
            return null;
        }
    }

    public BuildText getBuildText() {
        return buildText;
    }

    public AbstractField createText(BuildText buildText) {
        this.buildText = buildText;
        return this;
    }

    public String getCSSClass_phone() {
        return CSSClass_phone;
    }

    public void setCSSClass_phone(String cSSClass_phone) {
        CSSClass_phone = cSSClass_phone;
    }

    @Override
    public Record getCurrent() {
        return source != null ? source.getCurrent() : new Record();
    }

    @Override
    public final boolean isReadonly() {
        if (readonly > -1)
            return readonly == 1;
        return source != null ? source.isReadonly() : false;
    }

    public AbstractField setReadonly(boolean readonly) {
        this.readonly = readonly ? 1 : 0;
        return this;
    }

    public String getValue() {
        return value;
    }

    public AbstractField setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean isAutocomplete() {
        return autocomplete;
    }

    public AbstractField setAutocomplete(boolean autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

    public boolean isAutofocus() {
        return autofocus;
    }

    public AbstractField setAutofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public AbstractField setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public AbstractField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public AbstractField setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public AbstractField setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.hidden) {
            outputInput(html);
        } else {
            UILabel label = new UILabel().setFor(this.getId()).setText(this.getName() + "：");
            label.output(html);
            outputInput(html);
            if (this.showStar) {
                html.println("<font>*</font>");
            }
            UISpan span = new UISpan();
            if (this.dialog != null && this.dialog.isOpen()) {
                UIUrl url = new UIUrl(span).setHref(dialog.getUrl());
                UIImage img = new UIImage(url);
                img.setSrc(this.icon != null ? this.icon : CDN.get(config.getClassProperty("icon", "")));
            }
            span.output(html);
        }
    }

    protected void outputInput(HtmlWriter html) {
        if ("textarea".equals(htmlTag)) {
            UITextarea input = new UITextarea(null);
            input.setId(this.getId());
            input.setName(this.getId());
            input.setReadonly(this.isReadonly());
            input.setCssStyle(resize ? "resize: none;" : null);
            StringBuffer sb = new StringBuffer();
            sb.append(this.required ? " required" : "");
            sb.append(this.autofocus ? " autofocus" : "");
            if (sb.length() > 0)
                input.writeProperty(null, sb.toString().trim());
            input.writeProperty("placeholder", this.placeholder);
            input.writeProperty("maxlength", maxlength > 0 ? maxlength : null);
            input.writeProperty("rows", rows > 0 ? rows : null);
            input.writeProperty("cols", cols > 0 ? cols : null);
            String value = this.getValue();
            input.setText(value != null ? value : this.getText());
            input.output(html);
            return;
        }

        UIInput input = new UIInput(null);
        input.setId(this.getId());
        input.setName(this.getId());
        input.setInputType(this.hidden ? "hidden" : this.getHtmType());
        if (this.hidden) {
            input.setValue(this.getText());
        } else {
            String value = this.getValue();
            input.setValue(value != null ? value : this.getText());
            input.setReadonly(this.isReadonly());
            StringBuffer sb = new StringBuffer();
            sb.append(this.required ? " required" : "");
            sb.append(this.autofocus ? " autofocus" : "");
            sb.append(this.multiple ? " multiple" : "");
            if (sb.length() > 0)
                input.writeProperty(null, sb.toString().trim());
            input.setCssClass(this.CSSClass_phone);
            input.setPlaceholder(this.placeholder);
            input.writeProperty("autocomplete", this.autocomplete ? "on" : "off");
            input.writeProperty("pattern", this.pattern);
            input.writeProperty("oninput", this.oninput);
            input.writeProperty("onclick", this.onclick);
        }
        input.output(html);
    }

    public DialogField getDialog() {
        return dialog;
    }

    public AbstractField setDialog(String dialogfun) {
        this.dialog = new DialogField(dialogfun);
        dialog.setInputId(this.getId());
        return this;
    }

    public AbstractField setDialog(String dialogfun, String... params) {
        this.dialog = new DialogField(dialogfun);
        dialog.setInputId(this.getId());
        for (String string : params) {
            this.dialog.add(string);
        }
        return this;
    }

    public void createUrl(BuildUrl build) {
        this.buildUrl = build;
    }

    public BuildUrl getBuildUrl() {
        return buildUrl;
    }

    public Title createTitle() {
        Title title = new Title();
        title.setName(this.getField());
        return title;
    }

    public void updateField() {
        this.updateValue(this.getId(), this.getField());
    }

    @Override
    public void updateValue(String id, String code) {
        if (source instanceof SearchSource)
            ((SearchSource) source).updateValue(id, code);
    }

    public String getOninput() {
        return oninput;
    }

    public AbstractField setOninput(String oninput) {
        this.oninput = oninput;
        return this;
    }

    public String getOnclick() {
        return onclick;
    }

    public AbstractField setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }

    @Deprecated
    public String getTitle() {
        return this.getName();
    }

    public boolean isVisible() {
        return visible;
    }

    public AbstractField setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isShowStar() {
        return showStar;
    }

    public AbstractField setShowStar(boolean showStar) {
        this.showStar = showStar;
        return this;
    }

    public String getString() {
        return getCurrent().getString(this.getField());
    }

    public boolean getBoolean() {
        String val = this.getString();
        return "1".equals(val) || "true".equals(val);
    }

    public boolean getBoolean(boolean def) {
        String val = this.getString();
        if (val == null) {
            return def;
        }
        return "1".equals(val) || "true".equals(val);
    }

    public int getInt() {
        String val = this.getString();
        if (val == null || "".equals(val)) {
            return 0;
        }
        return Integer.parseInt(val);
    }

    public int getInt(int def) {
        String val = this.getString();
        if (val == null || "".equals(val)) {
            return def;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return def;
        }
    }

    public double getDouble() {
        String val = this.getString();
        if (val == null || "".equals(val)) {
            return 0;
        }
        return Double.parseDouble(val);
    }

    public double getDouble(double def) {
        String val = this.getString();
        if (val == null || "".equals(val)) {
            return def;
        }
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return def;
        }
    }

    public Datetime getDatetime() {
        return new Datetime(this.getString());
    }

    @Deprecated
    public Datetime getDateTime() {
        return new Datetime(this.getString());
    }

    public FastDate getDate() {
        return new FastDate(this.getString());
    }

    public String getString(String def) {
        String result = this.getString();
        return result != null ? result : def;
    }

    public FastDate getDate(Datetime def) {
        FastDate result = this.getDate();
        return result.isEmpty() ? def.toFastDate() : result;
    }

    public Datetime getDatetime(Datetime def) {
        Datetime result = this.getDatetime();
        return result.isEmpty() ? def : result;
    }

    @Deprecated
    public Datetime getDateTime(Datetime def) {
        Datetime result = this.getDateTime();
        return result.isEmpty() ? def : result;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHtmlTag() {
        return htmlTag;
    }

    public AbstractField setHtmlTag(String htmlTag) {
        this.htmlTag = htmlTag;
        return this;
    }

    public int getMaxlength() {
        return maxlength;
    }

    public AbstractField setMaxlength(int maxlength) {
        this.maxlength = maxlength;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public AbstractField setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public int getCols() {
        return cols;
    }

    public AbstractField setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public class Editor {
        private String xtype;

        public Editor(String xtype) {
            super();
            this.xtype = xtype;
        }

        public String getXtype() {
            return xtype;
        }

        public void setXtype(String xtype) {
            this.xtype = xtype;
        }
    }

    public class Title {
        private String name;
        private String type;
        private String dateFormat;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("name", this.name);
            if (this.type != null) {
                json.put("type", this.type);
            }
            if (this.dateFormat != null) {
                json.put("dateFormat", this.dateFormat);
            }
            return json.toString().replace("\"", "'");
        }
    }

    public void outputOfFormHorizontal(HtmlWriter html) {
        if (this.isHidden()) {
            this.output(html);
            return;
        }
        UIText mark = this.getMark();
        if (mark != null) {
            UILi li1 = new UILi(null);
            li1.writeProperty("role", this.getId());
            if (this instanceof ExpendField)
                li1.setCssClass("select");
            li1.setText(this.toString());
            UIUrl url = new UIUrl(li1);
            url.setHref(String.format("javascript:displaySwitch(\"%s\")", this.getId()));
            new UIImage(url).setSrc(CDN.get(config.getClassProperty("icon", "")));
            li1.output(html);
            //
            UILi li2 = new UILi(null);
            li2.writeProperty("role", this.getId()).setCssStyle("display: none;");
            li2.setText(mark.setRootLabel("mark").toString()).output(html);
        } else {
            UILi li = new UILi(null);
            li.writeProperty("role", this.getRole());
            if (this instanceof ExpendField)
                li.setCssClass("select");
            li.setText(this.toString()).output(html);
        }
    }

    public void outputOfGridLine(HtmlWriter html) {
        BuildUrl build = this.getBuildUrl();
        if (build != null) {
            UrlRecord url = new UrlRecord();
            build.buildUrl(this.getCurrent(), url);
            if (!"".equals(url.getUrl())) {
                UIUrl item = new UIUrl();
                item.setHref(url.getUrl());
                item.writeProperty("title", url.getTitle());
                item.setTarget(url.getTarget());
                item.setOnclick(String.format("return confirm(\"%s\");", url.getHintMsg()));
                item.setText(this.getText());
                item.output(html);
            } else {
                html.print(this.getText());
            }
        } else {
            html.print(this.getText());
        }
    }

}
