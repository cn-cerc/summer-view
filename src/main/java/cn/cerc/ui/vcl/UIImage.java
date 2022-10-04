package cn.cerc.ui.vcl;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.other.AliyunOssProcess;
import cn.cerc.ui.page.StaticFile;
import cn.cerc.ui.page.StaticFileType;

public class UIImage extends UIComponent implements IHtml {
    private String src;
    private AliyunOssProcess process;
    private String staticPath;

    public UIImage() {
        this(null);
    }

    public UIImage(UIComponent owner) {
        super(owner);
        this.setRootLabel("img");
        this.staticPath = Application.getStaticPath();
    }

    @Override
    public void output(HtmlWriter html) {
        String url = this.src;
        if (this.staticPath != null && this.process != null && !Utils.isEmpty(process.getCommand()))
            url += String.format("?x-oss-process=image%s", process.getCommand());
        this.setCssProperty("src", new StaticFile(StaticFileType.imageFile, url).toString());
        html.print("<").print(getRootLabel());
        this.outputPropertys(html);
        html.print("/>");
    }

    public String getSrc() {
        return src;
    }

    public UIImage setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 阿里云OSS图片压缩
     * 
     * @param width 阿里云格式化后的图片宽度，宽高比按照原图自动调整
     * @return 图片对象本身
     */
    public UIImage setSrc(String src, int width) {
        this.src = src;
        this.getProcess().setWidth(width);
        return this;
    }

    public String getWidth() {
        return (String) this.getCssProperty("width");
    }

    public UIImage setWidth(String width) {
        this.setCssProperty("width", width);
        return this;
    }

    public String getHeight() {
        return (String) this.getCssProperty("height");
    }

    public UIImage setHeight(String height) {
        this.setCssProperty("height", height);
        return this;
    }

    public String getRole() {
        return (String) this.getCssProperty("role");
    }

    public UIImage setRole(String role) {
        this.setCssProperty("role", role);
        return this;
    }

    public String getOnclick() {
        return (String) this.getCssProperty("onclick");
    }

    public UIImage setOnclick(String onclick) {
        this.setCssProperty("onclick", onclick);
        return this;
    }

    public String getAlt() {
        return (String) this.getCssProperty("alt");
    }

    public UIImage setAlt(String alt) {
        this.setCssProperty("alt", alt);
        return this;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public AliyunOssProcess getProcess() {
        if (process == null)
            process = new AliyunOssProcess();
        return process;
    }

    public void setProcess(AliyunOssProcess process) {
        this.process = process;
    }

}
