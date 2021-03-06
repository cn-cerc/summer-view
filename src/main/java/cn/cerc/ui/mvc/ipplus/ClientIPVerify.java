package cn.cerc.ui.mvc.ipplus;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.core.Application;

/**
 * 客户端IP地址校验，需要先打开 app.ip.filter 参数
 */
public class ClientIPVerify {
    private static final Logger log = LoggerFactory.getLogger(ClientIPVerify.class);

    private static final String filePath;

    static {
        // 加载本地文件配置
        String path = System.getProperty("user.home") + System.getProperty("file.separator");
        filePath = path + "IP_trial_single_WGS84.awdb";
    }

    private static final IClientIPCheckList client = Application.getBean(IClientIPCheckList.class);

    public static boolean allow(String ip) {
        File file = new File(filePath);
        // 开发环境下没有离线库文件则免校验
        if (ServerConfig.isServerDevelop()) {
            if (!file.exists())
                return true;
        }

        try (AWReader awReader = new AWReader(file)) {
            InetAddress address = InetAddress.getByName(ip);
            JsonNode record = awReader.get(address);

            if (record == null) {
                return false;
            }

            if (client == null) {
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
        } catch (IpTypeException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

}
