package cn.cerc.ui.page;

import cn.cerc.mis.core.Application;

public final class StaticFile {
    private final String fileName;
    private final String device;

    public StaticFile(String fileName, String device) {
        super();
        this.fileName = fileName;
        this.device = device;
    }

    protected final String getFileName() {
        return fileName;
    }

    protected final String getDevice() {
        return device;
    }

    public String getUrl(String version) {
        String staticPath = Application.getStaticPath();
        String[] args = fileName.split("\\.");
        if (device == null || "".equals(device))
            return String.format("%s%s.%s?v=%s", staticPath, args[0], args[1], version);
        else
            return String.format("%s%s-%s.%s?v=%s", staticPath, args[0], device, args[1], version);
    }
}