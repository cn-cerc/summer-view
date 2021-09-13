package cn.cerc.ui.vcl;

import cn.cerc.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.other.AliyunOssProcess;

public class UIImage extends UIComponent implements IHtml {
    private String src;
    private AliyunOssProcess process;
    private String staticPath;

    @Deprecated
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
        String url = this.staticPath != null ? staticPath : "";
        url += this.src;
        if (this.staticPath != null && this.process != null && !Utils.isEmpty(process.getCommand()))
            url += String.format("?x-oss-process=image%s", process.getCommand());
        this.writeProperty("src", url);
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
        return (String) this.readProperty("width");
    }

    public UIImage setWidth(String width) {
        this.writeProperty("width", width);
        return this;
    }

    public String getHeight() {
        return (String) this.readProperty("height");
    }

    public UIImage setHeight(String height) {
        this.writeProperty("height", height);
        return this;
    }

    public String getRole() {
        return (String) this.readProperty("role");
    }

    public UIImage setRole(String role) {
        this.writeProperty("role", role);
        return this;
    }

    public String getOnclick() {
        return (String) this.readProperty("onclick");
    }

    public UIImage setOnclick(String onclick) {
        this.writeProperty("onclick", onclick);
        return this;
    }

    public String getAlt() {
        return (String) this.readProperty("alt");
    }

    public UIImage setAlt(String alt) {
        this.writeProperty("alt", alt);
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
