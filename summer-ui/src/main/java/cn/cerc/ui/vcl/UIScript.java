package cn.cerc.ui.vcl;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/*
 * 专用于内嵌脚本输出
 */
public class UIScript extends UIComponent {
    private List<String> lines;
    private String modulePath;

    public UIScript(UIComponent owner) {
        super(owner);
        this.setRootLabel("script");
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        html.print("\n<").print(getRootLabel()).print(" type='module'>");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        for (String text : this.getLines())
            html.println(text);
        this.endOutput(html);
    }

    public UIScript add(String text) {
        this.getLines().add(text);
        return this;
    }

    public UIScript add(String format, Object... args) {
        return add(String.format(format, args));
    }

    public List<String> getLines() {
        if (lines == null)
            lines = new ArrayList<>();
        return lines;
    }

    public final String getModulePath() {
        return modulePath;
    }

    public UIScript setModulePath(String modulePath) {
        this.modulePath = modulePath;
        this.writeProperty("type", "module");
        return this;
    }

    public UIScript importModule(String moduleCode, String fileName) {
        this.add("import %s from \"%s/%s\";", moduleCode, this.modulePath, fileName);
        return this;
    }

}
