package cn.cerc.ui.sci;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIDiv;

public class UIReact extends UIComponent {
    private UIDiv content;
    private UIScriptContent script;

    public UIReact(UIComponent owner, String id) {
        super(owner);
        this.setId(id);
        this.content = new UIDiv(this);
        this.script = new UIScriptContent(this);
        this.script.setRootLabel("script");
        this.script.writeProperty("type", "text/babel");
//        addScriptFile("https://cdn.bootcdn.net/ajax/libs/react/16.13.1/umd/react.production.min.js");
//        addScriptFile("https://cdn.bootcdn.net/ajax/libs/react-dom/16.13.1/umd/react-dom.production.min.js");
//        addScriptFile("https://cdn.bootcdn.net/ajax/libs/babel-standalone/7.0.0-beta.3/babel.min.js");
    }
//
//    private UIReact addScriptFile(String fileName) {
//        UIComponent root = this.getOwner();
//        while (root != null) {
//            if (root instanceof SupportScriptFile) {
//                SupportScriptFile page = (SupportScriptFile) root;
//                page.addScriptFile(fileName);
//                break;
//            }
//            root = root.getOwner();
//        }
//        return this;
//    }

    public UIReact add(String text) {
        this.script.add(text);
        return this;
    }

    public UIReact addRender(String reactText) {
        this.add("ReactDOM.render(%s, document.getElementById(\"%s\"));", reactText, this.getId());
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
        this.content.setId(this.getId());
        super.beginOutput(html);
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

    public void addReact(String value, Object... args) {
        String react = String.format(value, args);
        this.add("ReactDOM.render(%s, document.getElementById(\"%s\"));", react, this.getId());

    }

}
