package cn.cerc.ui.page;

import java.util.Optional;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.core.Application;

public class StaticFile {
    private String object;
    private String endpoint;
    private String bucket = "";

    public StaticFile(String object) {
        // 表示开启了 CDN 或者 静态仓库
        this.endpoint = Application.getStaticPath();
        if (endpoint.startsWith("http"))
            this.bucket = "common/";
        this.object = object;
    }

    @Override
    public String toString() {
        // 文件自带 HTTP 网址则直接返回
        if (object.toLowerCase().startsWith("http"))
            return object;

        StringBuilder builder = new StringBuilder();
        builder.append(this.endpoint).append("/").append(this.bucket).append(this.object);

        // 取得版本号
        StaticFileVersionImpl impl = Application.getBean(StaticFileVersionImpl.class);
        if (impl != null) {
            Optional<Integer> version = impl.getVersion(this.bucket + this.object);
            if (version.isPresent())
                builder.append("?v=").append(version.get());
        }
        return builder.toString();
    }

    public String toProductString() {
        this.bucket = ServerConfig.getAppProduct() + "/";
        return this.toString();
    }

    public String toOriginalString() {
        this.bucket = ServerConfig.getAppOriginal() + "/";
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

    public static String getCommonFile(String fileName) {
        return new StaticFile(fileName).toString();
    }

    public static String getProductFile(String fileName) {
        return new StaticFile(fileName).toProductString();
    }

    public static String getOriginalFile(String fileName) {
        return new StaticFile(fileName).toOriginalString();
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