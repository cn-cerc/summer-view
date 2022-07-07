package cn.cerc.ui.page;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.core.Application;

public final class StaticFile {
    private String fileName;
    private String device;
    private StaticFileType fileType;
    private String fileRoot;
    private String filePath;

    private static final int Level_1 = 1;
    private static final int Level_2 = 1;
    private static final int Level_3 = 1;

    public enum StaticFileType {
        icon(Level_1),
        productImage(Level_1),
        otherImage(Level_1),
        cssFile(Level_2),
        jsFile(Level_2),
        summerImage(Level_3),
        menuImage(Level_2);

        private int version;

        public int getVersion() {
            return version;
        }

        StaticFileType(int version) {
            this.version = version;
        }

    }

    @Deprecated
    public StaticFile(String fileName, String device) {
        super();
        this.fileName = fileName;
        this.device = device;
    }

    public StaticFile(StaticFileType fileType, String fileName) {
        super();
        this.fileRoot = Application.getStaticPath();
        if (ServerConfig.isServerDevelop())
            this.fileRoot = "/static/";
        this.fileName = fileName;
        switch (fileType) {
        case cssFile:
            filePath = "";
            break;
        case otherImage:
            filePath = "";
            break;
        case summerImage:
            filePath = "";
            break;
        case menuImage:
            filePath = "menu/";
            break;
        default:
            filePath = "";
        }
        this.fileType = fileType;
    }

    public static String getCssFile(String fileName) {
        return new StaticFile(StaticFileType.cssFile, fileName).toString();
    }

    public static String getImage(String fileName) {
        String file = fileName;
        if (!fileName.toLowerCase().startsWith("http"))
            file = new StaticFile(StaticFileType.jsFile, fileName).toString();
        return file;
    }

    public static String getSummerImage(String fileName) {
        String file = fileName;
        if (!fileName.toLowerCase().startsWith("http"))
            file = new StaticFile(StaticFileType.jsFile, fileName).toString();
        return file;
    }

    public static String getMenuImage(String fileName) {
        String file = fileName;
        if (!fileName.toLowerCase().startsWith("http"))
            file = new StaticFile(StaticFileType.menuImage, fileName).toString();
        return file;
    }

    public static String getJsFile(String fileName) {
        String file = fileName;
        if (!fileName.toLowerCase().startsWith("http"))
            file = new StaticFile(StaticFileType.jsFile, fileName).toString();
        return file;
    }

    @Override
    public String toString() {
        if (this.fileType.getVersion() > 0)
            return String.format("%s%s%s?v=%d", this.fileRoot, this.filePath, this.fileName,
                    this.fileType.getVersion());
        else
            return String.format("%s%s%s", this.fileRoot, this.filePath, this.fileName);
    }

    public String getFileName() {
        return fileName;
    }

    @Deprecated
    protected String getDevice() {
        return device;
    }

    @Deprecated
    public String getUrl(String version) {
        String staticPath = Application.getStaticPath();
        String[] args = fileName.split("\\.");
        if (device == null || "".equals(device))
            return String.format("%s%s.%s?v=%s", staticPath, args[0], args[1], version);
        else
            return String.format("%s%s-%s.%s?v=%s", staticPath, args[0], device, args[1], version);
    }
}