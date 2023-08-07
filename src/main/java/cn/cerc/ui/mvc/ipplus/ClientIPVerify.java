package cn.cerc.ui.mvc.ipplus;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cn.cerc.mis.core.Application;

/**
 * 客户端IP地址校验，需要先打开 app.ip.filter 参数
 */
public class ClientIPVerify {
    private static final Logger log = LoggerFactory.getLogger(ClientIPVerify.class);

    private static final File file;

    static {
        // 加载本地文件配置
        String path = System.getProperty("user.home") + System.getProperty("file.separator");
        String filePath = path + "IP_trial_single_WGS84.awdb";
        file = new File(filePath);
    }

    private static final IClientIPCheckList client = Application.getBean(IClientIPCheckList.class);

    public static boolean allow(String ip) {
        // 没有定义则免校验
        if (client == null)
            return true;

        // 没有文件则免校验
        if (!file.exists())
            return true;

        // 检查地址合法性
        if (!isIPv4(ip)) {
            log.warn("{} 非法地址", ip);
            return false;
        }

        JsonNode record;
        try (AWReader awReader = new AWReader(file)) {
            InetAddress address = InetAddress.getByName(ip);
            record = awReader.get(address);
            if (record == null) {
                log.error("{} IP地址读取不到地址解析", ip);
                return false;
            }
        } catch (IpTypeException | IOException e) {
            log.error("IP {} 解析地址失败 => {}", ip, e.getMessage(), e);
            return true;
        }

        // 检查大洲通行的白名单
        if (record.has("continent")) {
            String continent = record.get("continent").asText();
            if ("保留IP".equals(continent))
                return true;
            if (client.getContinentWhiteList().stream().noneMatch(continent::contains)) {
                log.info("非法大洲ip {} {}", ip, record);
                return false;
            }
        }
        // 检查国家通行的白名单
        if (record.has("country")) {
            String country = record.get("country").asText();
            if (client.getCountryWhitelist().stream().noneMatch(country::contains)) {
                log.info("非法国家ip {} {}", ip, record);
                return false;
            }
        }
        // 检查省份通行的黑名单
        if (record.has("province")) {
            String province = record.get("province").asText();
            if (client.getProvinceBlacklist().stream().anyMatch(province::contains)) {
                log.info("非法省份ip {} {}", ip, record);
                return false;
            }
        }
        return true;
    }

    /**
     * 检查IPv4合法性
     */
    public static boolean isIPv4(String ip) {
        // 检查是否有4个地址部分
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        // 检查每个部分是否是有效的数字
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255)
                    return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String ip = "193.111.250.21";
        try (AWReader awReader = new AWReader(file)) {
            InetAddress address = InetAddress.getByName(ip);
            JsonNode record = awReader.get(address);
            System.out.println(record);
        } catch (IpTypeException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
