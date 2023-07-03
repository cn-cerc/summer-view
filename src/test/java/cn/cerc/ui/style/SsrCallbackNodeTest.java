package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class SsrCallbackNodeTest {

    @Test
    public void test_error() {
        SsrTemplate ssr = new SsrTemplate("begin:${callback}:end");
        assertEquals("begin:${callback}:end", ssr.getHtml());
    }

    @Test
    public void test() {
        SsrTemplate ssr = new SsrTemplate("begin:${callback(child)}:end");
        ssr.setCallback(field -> field + " ok");
        assertEquals("begin:child ok:end", ssr.getHtml());
    }

    @Test
    public void sample() {
        // 父级
        SsrTemplate master = new SsrTemplate("begin:${callback(child)}:end");

        // 子级
        SsrTemplate child = new SsrTemplate("<a href='${url}'>${title}</a>")
                .setMap(Map.of("url", "http://www.baidu.com", "title", "百度"));

        master.setCallback(field -> child.getHtml());

        assertEquals("begin:<a href='http://www.baidu.com'>百度</a>:end", master.getHtml());
    }

}
