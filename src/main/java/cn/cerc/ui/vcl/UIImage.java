package cn.cerc.ui.vcl;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.other.AliyunOssProcess;
import cn.cerc.ui.page.StaticFile;
import cn.cerc.ui.page.StaticFileType;

public class UIImage extends UIComponent implements IHtml {
    private String src;
    private AliyunOssProcess process;

    public UIImage() {
        this(null);
    }

    public UIImage(UIComponent owner) {
        super(owner);
        this.setRootLabel("img");
    }

    @Override
    public void output(HtmlWriter html) {
        this.setCssProperty("src", this.src);
        html.print("<").print(getRootLabel());
        this.outputPropertys(html);
        html.print("/>");
    }

    public String getSrc() {
        return src;
    }

    public UIImage setSrc(String src) {
        if (this.process != null && !Utils.isEmpty(process.getCommand()))
            src += String.format("?x-oss-process=image%s", process.getCommand());
        this.src = new StaticFile(StaticFileType.imageFile, src).toString();
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

    public AliyunOssProcess getProcess() {
        if (process == null)
            process = new AliyunOssProcess();
        return process;
    }

    public void setProcess(AliyunOssProcess process) {
        this.process = process;
    }

}
