package cn.cerc.ui.ssr.core;

import java.util.Map;

public class SsrMapProxy {
    private Map<String, String> map;
    private int index;

    public SsrMapProxy(Map<String, String> map) {
        this.map = map;
        index = -1;
    }

    public void first() {
        index = -1;
    }

    public boolean fetch() {
        index++;
        return map != null && index > -1 && index < map.size();
    }

    public String index() {
        return "" + index;
    }

    public String key() {
        if (map != null) {
            var i = 0;
            for (var key : map.keySet()) {
                if (i == index)
                    return key;
                i++;
            }
        }
        return null;
    }

    public String value() {
        if (map != null) {
            var i = 0;
            for (var key : map.keySet()) {
                if (i == index)
                    return map.get(key);
                i++;
            }
        }
        return null;
    }
}
