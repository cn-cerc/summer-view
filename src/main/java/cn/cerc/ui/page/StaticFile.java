package cn.cerc.ui.page;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;

public class StaticFile {
    private String object;
    private StaticFileType fileType;
    private String endpoint;
    private String bucket = "/";
    @Deprecated
    private String device = "";
    private boolean special;

    public StaticFile(StaticFileType fileType, String object) {
        this.fileType = fileType;
        // 表示开启了 CDN 或者 静态仓库
        this.endpoint = Application.getStaticPath();
        if (endpoint.startsWith("http"))
            this.bucket = "/common/";
        this.object = object;
    }

    @Override
    public String toString() {
        if (this.special)
            return object;

        // 文件自带 HTTP 网址则直接返回
        if (object.toLowerCase().startsWith("http"))
            return object;

        StringBuilder builder = new StringBuilder();
        builder.append(this.endpoint).append(this.bucket).append(this.object);

        // 取得版本号
        if (Application.containsBean(StaticFileVersionImpl.class)) {
            StaticFileVersionImpl impl = Application.getBean(StaticFileVersionImpl.class);
            if (impl != null) {
                String version = impl.getVersion(this.fileType.getGroup());
                if (!Utils.isEmpty(version))
                    builder.append("?v=").append(version);
            }
        }
        return builder.toString();
    }

    public String toProductString() {
        this.bucket = "/" + ServerConfig.getAppProduct() + "/";
        return this.toString();
    }

    public String toOriginalString() {
        this.bucket = "/" + ServerConfig.getAppOriginal() + "/";
        return this.toString();
    }

    public String getFileName() {
        return object;
    }

    protected String getFileRoot() {
        return endpoint;
    }

    protected void setFileRoot(String fileRoot) {
        this.endpoint = fileRoot;
    }

    private static String replace(String path) {
        int site = path.lastIndexOf("resources-");
        if (site > 0)
            path = path.substring(0, site) + "resources/";
        else {
            site = path.lastIndexOf("static-");
            if (site > 0)
                path = path.substring(0, site) + "static/";
        }
        return path;
    }

    public static String getCssFile(String fileName) {
        return new StaticFile(StaticFileType.cssFile, fileName).toString();
    }

    public static String getProductCssFile(String fileName) {
        return new StaticFile(StaticFileType.cssFile, fileName).toProductString();
    }

    public static String getOriginalCssFile(String fileName) {
        return new StaticFile(StaticFileType.cssFile, fileName).toProductString();
    }

    public static String getImage(String fileName) {
        return new StaticFile(StaticFileType.imageFile, fileName).toString();
    }

    public static String getProductImage(String fileName) {
        return new StaticFile(StaticFileType.productImage, fileName).toProductString();
    }

    public static String getOriginalImage(String fileName) {
        return new StaticFile(StaticFileType.imageFile, fileName).toOriginalString();
    }

    public static String getSummerImage(String fileName) {
        return new StaticFile(StaticFileType.summerImage, fileName).toString();
    }

    public static String getProductSummerImage(String fileName) {
        return new StaticFile(StaticFileType.summerImage, fileName).toProductString();
    }

    public static String getOriginalSummerImage(String fileName) {
        return new StaticFile(StaticFileType.summerImage, fileName).toOriginalString();
    }

    public static String getMenuImage(String fileName) {
        return new StaticFile(StaticFileType.menuImage, fileName).toString();
    }

    public static String getProductMenuImage(String fileName) {
        return new StaticFile(StaticFileType.menuImage, fileName).toProductString();
    }

    public static String getOriginalMenuImage(String fileName) {
        return new StaticFile(StaticFileType.menuImage, fileName).toOriginalString();
    }

    public static String getJsFile(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toString();
    }

    public static String getProductJsFile(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toProductString();
    }

    public static String getOriginalJsFile(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toOriginalString();
    }

    @Deprecated
    protected String getDevice() {
        return device;
    }

    public StaticFile setDevice(String device) {
        this.device = device;
        return this;
    }

    public static void main(String[] args) {
        System.out.println(StaticFile.replace(""));
        System.out.println(StaticFile.replace("/static/"));
        System.out.println(StaticFile.replace("/static-fpl/"));
        System.out.println(StaticFile.replace("https://vinetest.oss-cn-hangzhou.aliyuncs.com/resources/"));
        System.out.println(StaticFile.replace("https://vinetest.oss-cn-hangzhou.aliyuncs.com/resources-fpl/"));
        System.out.println(StaticFile.replace("https://vinetest.oss-cn-hangzhou.aliyuncs.com/static/"));
        System.out.println(StaticFile.replace("https://vinetest.oss-cn-hangzhou.aliyuncs.com/static-fpl/"));
    }

    public boolean isSpecial() {
        return special;
    }

    public StaticFile setSpecial(boolean special) {
        this.special = special;
        return this;
    }

}