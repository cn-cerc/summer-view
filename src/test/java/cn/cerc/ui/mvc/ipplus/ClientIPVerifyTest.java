package cn.cerc.ui.mvc.ipplus;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class ClientIPVerifyTest {

    @Test
    public void test_reserved() {
        String ip = "192.168.1.128";// 保留IP
        assertEquals(true, ClientIPVerify.allow(ip));
    }

    @Test
    public void test_abroad() {
        String ip = "113.180.74.76";// 越南
        assertEquals(true, ClientIPVerify.allow(ip));
    }

    @Test
    public void test_hk() {
        String ip = "123.58.212.8";// 香港
        assertEquals(true, ClientIPVerify.allow(ip));
    }

    @Test
    public void test_tw() {
        String ip = "36.231.69.50";// 台湾
        assertEquals(true, ClientIPVerify.allow(ip));
    }

    @Test
    public void test_random() {
        Random random = new Random();
        int a = random.nextInt(255);
        int b = random.nextInt(255);
        int c = random.nextInt(255);
        int d = random.nextInt(255);
        String ip = a + "." + b + "." + c + "." + d;
        assertEquals(true, ClientIPVerify.allow(ip));
    }

    @Test
    public void test_isIPv4() {
        assertEquals(false, ClientIPVerify.isIPv4("a"));
        assertEquals(false, ClientIPVerify.isIPv4("10.0.0.0/8"));
        assertEquals(false, ClientIPVerify.isIPv4("256.0.0.1"));
        assertEquals(false, ClientIPVerify.isIPv4("abc.def.ghi.jkl"));

        assertEquals(true, ClientIPVerify.isIPv4("192.168.1.1"));
        assertEquals(true, ClientIPVerify.isIPv4("193.111.250.21"));
    }

}