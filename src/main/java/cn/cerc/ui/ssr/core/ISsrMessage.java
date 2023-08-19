package cn.cerc.ui.ssr.core;

public interface ISsrMessage {

    /** 通知接收 */
    void onMessage(Object sender, int msgType, Object msgData, String targetId);

}
