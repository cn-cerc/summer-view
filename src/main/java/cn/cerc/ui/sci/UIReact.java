package cn.cerc.ui.sci;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.page.StaticFile;
import cn.cerc.ui.vcl.UIDiv;

public class UIReact extends UIComponent {
    private UIDiv content;
    private UIScriptContent script;
    private Set<String> reactList = new HashSet<>();

    public UIReact(UIComponent owner, String id) {
        super(owner);
        this.setId(id);
        this.content = new UIDiv(this);
        this.script = new UIScriptContent(this);
        this.script.setRootLabel("script");
        this.script.setCssProperty("type", "text/javascript");
    }

    public UIReact add(String text) {
        this.script.add(text);
        return this;
    }

    @Override
    public UIReact setId(String value) {
        super.setId(value);
        return this;
    }

    public UIReact add(String format, Object... args) {
        return add(String.format(format, args));
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        if (this.reactList.size() > 0) {
            for (String react : this.reactList) {
                html.println("<script src='%s'></script>",
                        getStaticFile(Application.getAuiPath(String.format("aui-%s.js", react))));
            }
        }
        this.content.setId(this.getId());
        super.beginOutput(html);
    }

    protected String getStaticFile(String filename) {
        return StaticFile.getProductJsFile(filename);
    }

    public class UIScriptContent extends UIComponent {
        private List<String> lines;

        public UIScriptContent(UIComponent owner) {
            super(owner);
            lines = new ArrayList<>();
        }

        @Override
        public void beginOutput(HtmlWriter html) {
            super.beginOutput(html);
            for (String line : lines)
                html.println(line);
        }

        public UIScriptContent add(String line) {
            this.lines.add(line);
            return this;
        }

    }

    public UIReact addReact(String name) {
        this.add("ReactDOM.render(<aui.%s />, document.getElementById(\"%s\"))", name, this.getId());
        reactList.add(name);
        return this;
    }

    public UIReact addReact(String name, DataRow row) {
        String props = "{";
        for (FieldMeta meta : row.fields().getItems()) {
            Object value = row.getValue(meta.code());
            if (value instanceof String)
                value = "'" + value + "'";
            props += String.format("%s: %s, ", meta.code(), value);
        }
        props += "}";
        this.add("aui.babel.loadAuiPage(aui.%s, %s, '%s')", name, props, this.getId());
//        this.add("ReactDOM.render(<aui.%s %s/>, document.getElementById(\"%s\"))", name, props, this.getId());
        reactList.add(name);
        return this;
    }

    public UIReact addReact(String name, String className, DataRow row) {
        String props = "";
        for (FieldMeta meta : row.fields().getItems()) {
            Object value = row.getValue(meta.code());
            if (value instanceof String)
                value = "'" + value + "'";
            props += String.format("%s={%s} ", meta.code(), value);
        }
        this.add("ReactDOM.render(<aui.%s.%s %s/>, document.getElementById(\"%s\"))", name, className, props,
                this.getId());
        reactList.add(name);
        return this;
    }

}
