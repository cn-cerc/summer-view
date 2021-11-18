package cn.cerc.ui.mvc.ipplus;

import cn.cerc.db.core.ServerConfig;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

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

            if (record == null) {
                return false;
            }

            JsonNode continent = record.get("continent");
            if (continent != null) {
                if ("保留IP".equals(continent.asText())) {// 允许保留ip通过
                    return true;
                }
            }
            // 检查国家
            JsonNode country = record.get("country");
            if (country != null) {
                if (!"中国".equals(country.asText())) {
                    log.warn("境外ip {} {}", ip, record);
                    return false;
                }
            }
            // 检查省份
            JsonNode province = record.get("province");
            if (province != null) {
                if (province.asText().contains("香港")) {
                    log.warn("香港ip {} {}", ip, record);
                    return false;
                }
                if (province.asText().contains("台湾")) {
                    log.warn("台湾ip {} {}", ip, record);
                    return false;
                }
            }
        } catch (IpTypeException |
                IOException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

}
