package cn.cerc.ui.fields;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.DataColumn;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSetSource;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.FastDate;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.SearchSource;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.other.BuildText;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UILabel;
import cn.cerc.ui.vcl.UISpan;
import cn.cerc.ui.vcl.UIText;
import cn.cerc.ui.vcl.UIUrl;

public abstract class AbstractField extends UIComponent implements INameOwner, SearchSource {
    public static final ClassConfig config = new ClassConfig(AbstractField.class, SummerUI.ID);
    // 数据库相关
    private String field;
    // 自定义取值
    private BuildText buildText;
    // 焦点否
    private boolean autofocus;
    //
    private boolean required;
    // 正则过滤
    private String pattern;
    //
    private boolean hidden;
    // 角色
    private String role;
    //
    private DialogField dialog;
    // dialog 小图标
    private String icon;
    //
    private BuildUrl buildUrl;
    // 数据源
    private DataSetSource source;

    private String oninput;
    private String onclick;
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
    // 是否显示*号
    private boolean showStar = false;
    // 字段标题
    private UILabel title;
    // 输入字段
    private UIInput content = new UIInput(this);
    // 列固定状态
    private StickyRow stickyRow = StickyRow.def;
    // 是否超出两行展示为省略号
    private boolean showEllipsis = false;
    // 名词id
    private Integer wordId;

    public AbstractField(UIComponent owner, String name, String field) {
        this(owner, name, field, 0);
    }

    public AbstractField(UIComponent owner, String name, String field, int width) {
        super(owner);
        this.setField(field);
        this.setName(name);
        this.width = width;
        this.title = new UILabel(this);
        // 查找最近的数据源
        UIComponent root = owner;
        while (root != null) {
            if (root instanceof DataSetSource) {
                this.source = (DataSetSource) root;
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

    public AbstractField setMark(String mark) {
        return setMark(new UIText().setText(mark));
    }

    public AbstractField setWordId(Integer id) {
        this.wordId = id;
        return this;
    }

    public Integer getWordId() {
        return wordId;
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
        DataRow record = this.current();
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
    public Optional<DataRow> currentRow() {
        DataRow result = null;
        if (source != null)
            result = source.getDataSet().map(ds -> ds.current()).orElse(null);
        if (result == null)
            result = new DataRow();
        return Optional.of(result);
    }

    @Override
    public final boolean readonly() {
        if (readonly > -1)
            return readonly == 1;
        return source != null ? source.readonly() : false;
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
        return (String) content.getCssProperty("placeholder");
    }

    public AbstractField setPlaceholder(String placeholder) {
        this.content.setCssProperty("placeholder", placeholder);
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
        title.setOwner(hidden ? null : this);
        return this;
    }

    public StickyRow getStickyRow() {
        return stickyRow;
    }

    public void setStickyRow(StickyRow stickyRow) {
        this.stickyRow = stickyRow;
    }

    public boolean isShowEllipsis() {
        return showEllipsis;
    }

    public void setShowEllipsis(boolean showEllipse) {
        this.showEllipsis = showEllipse;
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        this.title.setFor(this.getId()).setText(this.getName() + "：");
        this.title.setOwner(visible ? this : null);
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        content.setId(this.getId());
        content.setName(this.getId());
        content.setInputType(this.hidden ? "hidden" : this.getHtmType());
        if (this.hidden) {
            content.setValue(this.getText());
        } else {
            this.title.output(html);
            String value = this.getValue();
            content.setCssClass(this.CSSClass_phone);
            content.setValue(value != null ? value : this.getText());
            content.setReadonly(this.readonly());
            content.setCssProperty("autocomplete", this.autocomplete ? "on" : "off");
            content.setCssProperty("pattern", this.pattern);
            content.setCssProperty("oninput", this.oninput);
            content.setCssProperty("onclick", this.onclick);
            content.setSignProperty("required", this.required);
            content.setSignProperty("autofocus", this.autofocus);
        }
        content.output(html);
        this.endOutput(html);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (this.showStar) {
            new UIStarFlag(null).output(html);
//            new UIFont(null).addComponent(new UIText().setText("*")).output(html);
        }
        if (!this.hidden) {
            UISpan span = new UISpan(null);
            if (this.dialog != null && this.dialog.isOpen()) {
                String src = this.icon != null ? this.icon : getIconConfig();
                UIUrl url = new UIUrl(span).setHref(dialog.getUrl());
                new UIImage(url).setSrc(src);
            }
            span.output(html);
        }
        super.endOutput(html);
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

    public interface BuildUrl {
        void buildUrl(DataRow record, UIUrl url);
    }

    public AbstractField createUrl(BuildUrl build) {
        this.buildUrl = build;
        return this;
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

    public UILabel getTitle() {
        return this.title;
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
        return current().getString(this.getField());
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

    public static String getIconConfig() {
        return config.getClassProperty("icon", "");
    }

    protected UIComponent getContent() {
        return this.content;
    }

    public enum StickyRow {
        def,
        left,
        right;
    }

    public DataColumn value() {
        return new DataColumn(this.currentRow().orElseThrow().dataSet(), this.getField());
    }
}
