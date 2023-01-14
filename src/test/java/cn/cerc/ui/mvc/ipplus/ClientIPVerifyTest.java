package cn.cerc.ui.mvc.ipplus;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.cerc.mis.core.Application;

@Ignore
public class ClientIPVerifyTest {

    @Before
    public void init() {
        Application.initOnlyFramework();
    }

    @Test
    public void test_reserved() {
        String ip = "192.168.1.128";// 保留IP
        Assert.assertTrue(ClientIPVerify.allow(ip));
    }

    @Test
    public void test_abroad() {
        String ip = "113.180.74.76";// 越南
        Assert.assertFalse(ClientIPVerify.allow(ip));
    }

    @Test
    public void test_hk() {
        String ip = "123.58.212.8";// 香港
        Assert.assertFalse(ClientIPVerify.allow(ip));
    }

    @Test
    public void test_tw() {
        String ip = "36.231.69.50";// 台湾
        Assert.assertFalse(ClientIPVerify.allow(ip));
    }

    @Test
    public void test_random() {
        String ip = getRandomIp();
        Assert.assertFalse(ClientIPVerify.allow(ip));
    }

    public static String getRandomIp() {
        Random random = new Random();
        int a = random.nextInt(255);
        int b = random.nextInt(255);
        int c = random.nextInt(255);
        int d = random.nextInt(255);
        return a + "." + b + "." + c + "." + d;
    }
}