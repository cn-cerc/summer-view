package cn.cerc.security.menu;

import cn.cerc.mis.core.HtmlWriter;

public interface IMenuItem {

    String getGroup();

    String getName();

    String getCode();

    String getProcCode();

    String getPermission();

    // 0: 开发中，1.使用中；2.已停用
    MenuStatusEnum getStatus();

    /**
     * 返回菜单的图标文件名
     * 
     * @return 返回值可能为 null，或.png扩展名
     */
    String getIcon();

    void output(HtmlWriter html);

}
