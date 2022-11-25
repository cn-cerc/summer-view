package cn.cerc.ui.page;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;

public class StaticFile {
    private String fileName;
    private StaticFileType fileType;
    private String fileRoot;
    @Deprecated
    private String device = "";

    public StaticFile(StaticFileType fileType, String fileName) {
        this(fileType, fileName, true);
    }

    public StaticFile(StaticFileType fileType, String fileName, boolean isCommonFile) {
        super();
        this.fileRoot = Application.getStaticPath();
        this.fileRoot += isCommonFile ? "/common" : "/" + ServerConfig.getAppTrademark();
        this.fileName = fileName;
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        if (fileName.toLowerCase().startsWith("http"))
            return fileName;

        StringBuilder builder = new StringBuilder();
        builder.append(this.fileRoot).append("/");
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
        return new StaticFile(StaticFileType.cssFile, fileName, false).toString();
    }

    public static String getImage(String fileName) {
        return new StaticFile(StaticFileType.imageFile, fileName).toString();
    }

    public static String getProductImage(String fileName) {
        return new StaticFile(StaticFileType.imageFile, fileName, false).toString();
    }

    public static String getSummerImage(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toString();
    }

    public static String getProductSummerImage(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName, false).toString();
    }

    public static String getMenuImage(String fileName) {
        return new StaticFile(StaticFileType.menuImage, fileName).toString();
    }

    public static String getProductMenuImage(String fileName) {
        return new StaticFile(StaticFileType.menuImage, fileName, false).toString();
    }

    public static String getJsFile(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName).toString();
    }

    public static String getProductJsFile(String fileName) {
        return new StaticFile(StaticFileType.jsFile, fileName, false).toString();
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