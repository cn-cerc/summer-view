package cn.cerc.db.core;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurlTest {

    @Test
    public void test() {
        String host = "http://smapi.sanmaoyou.com/api/tips/list/v2";
//        String host = "https://www.jayun.site/api/message";

        Curl curl = new Curl();
        log.info(curl.sendGet(host));
    }

    // @Test
    public void test_param() {
//        https://tf.sanmaoyou.com/api/tips/list/v2?utm_tid=126&city_id=&country_id=5043&keyword=&page=1&page_size=10

        Curl curl = new Curl();
        curl.putParameter("utm_tid", 126);
        curl.putParameter("country_id", 5043);

        String host = "http://smapi.sanmaoyou.com/api/tips/list/v2";
        log.info(curl.sendGet(host));
    }

}
