package cn.cerc.ui.mvc.ipplus;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class ClientIPVerifyTest {

    @Test
    public void test_abroad() {
        String ip = "113.180.74.76";// 越南
        Assert.assertFalse(ClientIPVerify.allowip(ip));
    }

    @Test
    public void test_hk() {
        String ip = "123.58.212.8";// 香港
        Assert.assertFalse(ClientIPVerify.allowip(ip));
    }

    @Test
    public void test_tw() {
        String ip = "36.231.69.50";// 台湾
        Assert.assertFalse(ClientIPVerify.allowip(ip));
    }

//    @Test
    public void test_random() {
        String ip = getRandomIp();
        Assert.assertFalse(ClientIPVerify.allowip(ip));
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