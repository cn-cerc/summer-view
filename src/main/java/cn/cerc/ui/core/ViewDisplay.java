package cn.cerc.ui.core;

/**
 * 视图组件显示
 */
public enum ViewDisplay {
    强制显示, // 用户不可以改配置
    选择显示, // 用户可以自己配置
    默认隐藏; // 用户看不见但有效
}