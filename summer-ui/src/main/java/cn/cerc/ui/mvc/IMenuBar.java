package cn.cerc.ui.mvc;

import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.UrlRecord;

import java.util.List;

public interface IMenuBar {
    // 登记菜单栏菜单项
    int enrollMenu(IForm form, List<UrlRecord> menus);

    // 购物车绑定菜单
    String buildMenusBar(AbstractForm form);
}
