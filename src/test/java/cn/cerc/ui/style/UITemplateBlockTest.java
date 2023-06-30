package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UITemplateBlockTest {

    @Test
    public void test() {
        var block = new UITemplateBlock(null, "<a href=${0}>${1}</a>");
        block.getTemplate().addItems("url", "name");
        assertEquals("<a href=url>name</a>", block.toString());
    }

}
