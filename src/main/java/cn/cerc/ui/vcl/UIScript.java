package cn.cerc.ui.vcl;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.page.StaticFile;

/*
 * 专用于内嵌脚本输出
 */
public class UIScript extends UIComponent {
    protected static final ClassConfig config = new ClassConfig(UIScript.class, SummerUI.ID);
    private List<String> lines;
    private String modulePath;
    private String src;

    public UIScript(UIComponent owner) {
        super(owner);
        this.setRootLabel("script");
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        if (this.src != null) {
            StaticFile sf = new StaticFile(this.src);
            this.setCssProperty("src", sf.toString());
        } else {
            this.setCssProperty("src", null);
        }
        html.println("");
        super.beginOutput(html);
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
        if (this.src != null)
            throw new RuntimeException("src is not null");

        this.modulePath = modulePath;
        this.setCssProperty("type", "module");
        return this;
    }

    public UIScript importModule(String moduleCode, String fileName) {
        if (this.src != null)
            throw new RuntimeException("src is not null");
        this.add("import %s from \"%s/%s\";", moduleCode, this.modulePath, fileName);
        return this;
    }

    public String getSrc() {
        return this.src;
    }

    public UIScript setSrc(String src) {
        this.src = src;
        return this;
    }

}
