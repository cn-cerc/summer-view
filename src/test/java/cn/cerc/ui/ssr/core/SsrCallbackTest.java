package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrCallbackTest {

    @Test
    public void test_error() {
        SsrBlock ssr = new SsrBlock("begin${callback}end");
        assertEquals("begin${callback}end", ssr.html());
    }

    @Test
    public void test() {
        SsrBlock ssr = new SsrBlock("begin:${callback(child)}:end");
        ssr.onCallback("child", () -> "child ok");
        assertEquals("begin:child ok:end", ssr.html());
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
        master.onCallback("child", () -> child.html());

        assertEquals(" <a href='http://www.baidu.com'>百度</a>", master.html());
    }

}
