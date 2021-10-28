package cn.cerc.ui.sci;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.SupportScriptFile;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIDiv;

public class UIReact extends UIComponent {
    private UIDiv content;
    private UIScriptContent script;

    public UIReact(UIComponent owner) {
        super(owner);
        this.content = new UIDiv(this);
        this.script = new UIScriptContent(this);
        this.script.setRootLabel("script");
        this.script.writeProperty("type", "text/babel");
        addJsFile("https://cdn.bootcdn.net/ajax/libs/react/16.13.1/umd/react.production.min.js");
        addJsFile("https://cdn.bootcdn.net/ajax/libs/react-dom/16.13.1/umd/react-dom.production.min.js");
        addJsFile("https://cdn.bootcdn.net/ajax/libs/babel-standalone/7.0.0-beta.3/babel.min.js");
    }

    private void addJsFile(String fileName) {
        UIComponent root = this.getOwner();
        while (root != null) {
            if (root instanceof SupportScriptFile) {
                SupportScriptFile page = (SupportScriptFile) root;
                page.addScriptFile(fileName);
                break;
            }
            root = root.getOwner();
        }
    }

    public UIReact add(String text) {
        this.getLines().add(text);
        return this;
    }

    public UIReact setRender(String jsFile, String reactClass, String id) {
        this.setId(id);
        this.addJsFile(jsFile);
        this.add("ReactDOM.render(<%s/>, document.getElementById(\"%s\"));", reactClass, this.getId());
        return this;
    }

    public UIReact add(String format, Object... args) {
        return add(String.format(format, args));
    }

    public List<String> getLines() {
        return script.getLines();
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        this.content.setId(this.getId());
        super.beginOutput(html);
    }

    public class UIScriptContent extends UIComponent {
        private String text;
        private List<String> lines;

        public UIScriptContent(UIComponent owner) {
            super(owner);
        }

        @Override
        public void beginOutput(HtmlWriter html) {
            super.beginOutput(html);
            if (text != null)
                html.print(text);
            if (lines != null) {
                for (String line : lines)
                    html.println(line);
            }
        }

        public List<String> getLines() {
            if (lines == null)
                lines = new ArrayList<>();
            return lines;
        }

        public UIScriptContent add(String line) {
            this.getLines().add(line);
            return this;
        }

    }

}
