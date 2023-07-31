package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrFormStyleDefaultTest {

    @Test
    public void test2() {
        var obj = new SsrFormStyleDefault();
        assertEquals("show('a')", obj.getDialogText("a", "show"));
    }

    @Test
    public void test3() {
        var obj = new SsrFormStyleDefault();
        assertEquals("show('a','b')", obj.getDialogText("a", "show", "b"));
    }

    @Test
    public void test4() {
        var obj = new SsrFormStyleDefault();
        assertEquals("show('a','b','c')", obj.getDialogText("a", "show", "b", "c"));
    }

}
