package cn.cerc.ui.ssr.core;

public record EntityServiceRecord(String service, String desc) {

    public static final EntityServiceRecord EMPTY = new EntityServiceRecord("", "");

}
