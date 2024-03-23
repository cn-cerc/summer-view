package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;

public interface ISupportFilter extends ISupportServiceField {

    SearchTypeEnum searchType();

    JoinDirectionEnum joinDirection();

    boolean endJoin();

}
