package cn.cerc.ui.mvc.ipplus;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cn.cerc.db.core.ServerConfig;

/**
 * 客户端IP地址校验
 */
public class ClientIPVerify {
    private static final Logger log = LoggerFactory.getLogger(ClientIPVerify.class);

    private static final String filePath;

    static {
        // 加载本地文件配置
        String path = System.getProperty("user.home") + System.getProperty("file.separator");
        filePath = path + "IP_trial_single_WGS84.awdb";
    }

    public static boolean allowip(String ip) {
        // 开发环境免校验
        if (ServerConfig.isServerDevelop()) {
            return true;
        }

        try {
            AWReader awReader = new AWReader(new File(filePath));
            InetAddress address = InetAddress.getByName(ip);
            JsonNode record = awReader.get(address);
            if (record != null) {
                JsonNode continent = record.get("continent");
                if (continent != null) {
                    if ("保留IP".equals(continent.asText())) {// 允许保留ip通过
                        return true;
                    }
                }

                JsonNode country = record.get("country");
                if (country != null) {
                    if (!"中国".equals(country.asText())) {
                        log.info("访问者ip {}", record);
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (IpTypeException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                new Thread(() -> {
                    String ip = getRandomIp();
                    System.out.println(ip);
                    System.out.println(ClientIPVerify.allowip(ip));
                }).start();
            }
        }
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
