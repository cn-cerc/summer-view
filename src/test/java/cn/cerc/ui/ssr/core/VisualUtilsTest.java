package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VisualUtilsTest {

    @Test
    public void test1() {
        assertEquals("a", SsrUtils.getBeanId("a"));
    }

    @Test
    public void test2() {
        assertEquals("aa", SsrUtils.getBeanId("aa"));
    }

    @Test
    public void test3() {
        assertEquals("ab", SsrUtils.getBeanId("Ab"));
    }

    @Test
    public void test4() {
        assertEquals("AB", SsrUtils.getBeanId("AB"));
    }

    @Test
    public void test5() {
        assertEquals("aB", SsrUtils.getBeanId("aB"));
    }

}
