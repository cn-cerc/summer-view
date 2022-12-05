package cn.cerc.ui.page;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;

public class StaticFile {
    private String fileName;
    private StaticFileType fileType;
    private String fileRoot;
    private String fileFolder;
    @Deprecated
    private String device = "";

    public StaticFile(StaticFileType fileType, String fileName) {
        this.fileRoot = Application.getStaticPath();

        // 表示开启了cdn或者调用静态仓库
        if (fileRoot.startsWith("http"))
            this.fileFolder = "common";
        else
            this.fileFolder = "";

        this.fileName = fileName;
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        if (fileName.toLowerCase().startsWith("http"))
            return fileName;

        String path = "/";
        if (!Utils.isEmpty(this.fileFolder))
            path = String.format("/%s/", this.fileFolder);

        StringBuilder builder = new StringBuilder();
        builder.append(this.fileRoot).append(path);// 在节点 common/cdn 之后加上 /
        builder.append(this.fileName);
        // 取得版本号
        StaticFileVersionImpl impl = Application.getBean(StaticFileVersionImpl.class);
        if (impl != null) {
            String version = impl.getVersion(this.fileType.getGroup());
            if (!Utils.isEmpty(version))
                builder.append("?v=").append(version);
        }
        return builder.toString();
    }

    public String toProductString() {
        this.fileFolder = ServerConfig.getAppProduct();
        return this.toString();
    }

    public String toOriginalString() {
        this.fileFolder = ServerConfig.getAppIndustry();
        return this.toString();
    }

    public String getFileName() {
        return fileName;
    }

    protected String getFileRoot() {
        return fileRoot;
    }

    protected void setFileRoot(String fileRoot) {
        this.fileRoot = fileRoot;
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
        return new StaticFile(StaticFileType.imageFile, fileName).toProductString();
    }

    public static String getOriginalImage(String fileName) {
        return new StaticFile(StaticFileType.imageFile, fileName).toOriginalString();
    }

    public static String getSummerImage(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toString();
    }

    public static String getProductSummerImage(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toProductString();
    }

    public static String getOriginalSummerImage(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toOriginalString();
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

    @Deprecated
    public String getUrl(String version) {
        String staticPath = Application.getStaticPath();
        String[] args = getFileName().split("\\.");
        if (device == null || "".equals(device))
            return String.format("%s%s.%s?v=%s", staticPath, args[0], args[1], version);
        else
            return String.format("%s%s-%s.%s?v=%s", staticPath, args[0], device, args[1], version);
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
}