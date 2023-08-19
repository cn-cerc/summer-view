package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.ui.ssr.core.SsrBlock;

public class SsrCallbackTest {

    @Test
    public void test_error() {
        SsrBlock ssr = new SsrBlock("begin${callback}end");
        assertEquals("begin${callback}end", ssr.getHtml());
    }

    @Test
    public void test() {
        SsrBlock ssr = new SsrBlock("begin:${callback(child)}:end");
        ssr.onCallback("child", () -> "child ok");
        assertEquals("begin:child ok:end", ssr.getHtml());
    }

    @Test
    public void sample() {
        // 父级
        SsrBlock master = new SsrBlock("""
                ${if createMode}
                    ${callback(child)}
                ${endif}
                """);

        // 子级
        SsrBlock child = new SsrBlock("<a href='${url}'>${title}</a>").toMap("url", "http://www.baidu.com")
                .toMap("title", "百度");

        master.toMap("createMode", "" + true);
        master.onCallback("child", () -> child.getHtml());

        assertEquals(" <a href='http://www.baidu.com'>百度</a>", master.getHtml());
    }

}
