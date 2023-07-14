package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrCallbackTest {

    @Test
    public void test_error() {
        SsrTemplate ssr = new SsrTemplate("begin${callback}end");
        assertEquals("begin${callback}end", ssr.getHtml());
    }

    @Test
    public void test() {
        SsrTemplate ssr = new SsrTemplate("begin:${callback(child)}:end");
        ssr.setCallback((sender) -> sender.getField() + " ok");
        assertEquals("begin:child ok:end", ssr.getHtml());
    }

    @Test
    public void sample() {
        // 父级
        SsrTemplate master = new SsrTemplate("""
                ${if createMode}
                    ${callback(child)}
                ${endif}
                """);

        // 子级
        SsrTemplateImpl child = new SsrTemplate("<a href='${url}'>${title}</a>").toMap("url", "http://www.baidu.com")
                .toMap("title", "百度");

        master.toMap("createMode", "" + true);
        master.setCallback(sender -> child.getHtml());

        assertEquals("<a href='http://www.baidu.com'>百度</a>", master.getHtml());
    }

}
