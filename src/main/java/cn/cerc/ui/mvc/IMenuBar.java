package cn.cerc.ui.mvc;

import java.util.List;

import cn.cerc.db.core.IHandle;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.UrlRecord;

public interface IMenuBar {
    // 登记菜单栏菜单项
    int enrollMenu(IForm form, List<UrlRecord> menus);

    // 购物车绑定菜单
    String buildMenusBar(AbstractForm form);

    // 购物车名称
    String buildShopName(IHandle handle, String tbType, boolean isPhone);
}
