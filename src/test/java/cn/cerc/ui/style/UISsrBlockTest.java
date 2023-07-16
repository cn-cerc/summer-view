package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UISsrBlockTest {

    @Test
    public void test() {
        var block = new UISsrBlock(null, "<a href=${0}>${1}</a>");
        block.getTemplate().toList("url", "name");
        assertEquals("<a href=url>name</a>", block.toString());
    }

}
