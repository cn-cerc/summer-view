package cn.cerc.ui.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlRecordTest {

    @Test
    public void encode() {
        /*
         * http://127.0.0.1/911001/TFrmPartImage.listPartImages?partCode=ZKYXBSB-A+-L
         */
        String str = "http://127.0.0.1/911001/TFrmPartImage.listPartImages";
        UrlRecord url = new UrlRecord();
        url.setSite(str);
        url.putParam("partCode", "ZKYXBSB-A+-L");
        assertEquals("http://127.0.0.1/911001/TFrmPartImage.listPartImages?partCode=ZKYXBSB-A%2B-L", url.getUrl());
    }

    @Test
    public void test() {
        UrlRecord url = UrlRecord.builder("TFrmUserMenu")
                .name("菜单设置")
                .title("这是系统菜单")
                .put("module", "TBase")
                .put("menuCode", "TFrmPartInfo")
                .put("标记", "中国")
                .build();
        assertEquals("TFrmUserMenu?标记=%E4%B8%AD%E5%9B%BD&module=TBase&menuCode=TFrmPartInfo", url.getUrl());
    }

}