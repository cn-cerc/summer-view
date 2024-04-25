package cn.cerc.ui.vcl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.page.StaticFile;
import cn.cerc.ui.page.StaticFileType;

public class UIImageTest {
    private String staticPath = Application.getStaticPath();
    private String product = ServerConfig.getAppProduct();
    private String original = ServerConfig.getAppOriginal();

    @Test
    public void test1() {
        UIImage image = new UIImage(null);
        image.setSrc("images/icon/a/png");
        assertEquals(String.format("<img src='%s/static/common/images/icon/a/png'/>", staticPath), image.toString());
    }

    @Test
    public void test2() {
        UIImage image = new UIImage(null);
        image.setSrc(new StaticFile(StaticFileType.imageFile, "images/icon/a/png").toProductString());
        assertEquals(String.format("<img src='%s/%s/images/icon/a/png'/>", staticPath, product), image.toString());
    }

    @Test
    public void test3() {
        UIImage image = new UIImage(null);
        image.setSrc(new StaticFile(StaticFileType.imageFile, "images/icon/a/png").toOriginalString());
        assertEquals(String.format("<img src='%s/%s/images/icon/a/png'/>", staticPath, original), image.toString());
    }
}
