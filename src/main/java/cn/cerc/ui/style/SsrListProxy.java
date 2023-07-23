package cn.cerc.ui.style;

import java.util.List;

public class SsrListProxy {
    private List<String> list;
    private int index;

    public SsrListProxy(List<String> list) {
        this.list = list;
    }

    public void first() {
        index = -1;
    }

    public boolean fetch() {
        index++;
        return list != null && index > -1 && index < list.size();
    }

    public String index() {
        return "" + index;
    }

    public String value() {
        if (list != null && index > -1 && index < list.size())
            return list.get(index);
        else
            return null;
    }
}
