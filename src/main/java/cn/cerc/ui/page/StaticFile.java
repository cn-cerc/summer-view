package cn.cerc.ui.page;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;

public final class StaticFile {
    private String fileName;
    private String device;
    private StaticFileType fileType;
    private String fileRoot;
    private String filePath;

    public enum StaticFileType {
        icon(StaticFileGroup.低频更新),
        productImage(StaticFileGroup.低频更新),
        otherImage(StaticFileGroup.低频更新),
        cssFile(StaticFileGroup.中频更新),
        jsFile(StaticFileGroup.中频更新),
        imageFile(StaticFileGroup.中频更新),
        summerImage(StaticFileGroup.高频更新),
        menuImage(StaticFileGroup.中频更新);

        private StaticFileGroup group;

        public StaticFileGroup getGroup() {
            return group;
        }

        StaticFileType(StaticFileGroup group) {
            this.group = group;
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
            file = new StaticFile(StaticFileType.imageFile, fileName).toString();
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
        String version = "";
        StaticFileVersionImpl impl = Application.getBean(StaticFileVersionImpl.class);
        if (impl != null) {
            version = impl.getVersion(this.fileType.getGroup());
            if (!Utils.isEmpty(version))
                return String.format("%s%s%s?v=%s", this.fileRoot, this.filePath, this.fileName, version);
        }
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