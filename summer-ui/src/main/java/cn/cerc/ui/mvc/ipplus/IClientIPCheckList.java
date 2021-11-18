package cn.cerc.ui.mvc.ipplus;

import java.util.HashSet;
import java.util.Set;

public interface IClientIPCheckList {

    /**
     * 获取大洲白名单
     */
    default Set<String> getContinentWhiteList() {
        return new HashSet<>();
    }

    /**
     * 获取国家白名单
     */
    default Set<String> getCountryWhitelist() {
        return new HashSet<>();
    }

    /**
     * 获取省份黑名单
     */
    default Set<String> getProvinceBlacklist() {
        return new HashSet<>();
    }

}
