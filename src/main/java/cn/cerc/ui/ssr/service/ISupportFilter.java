package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;

public interface ISupportFilter extends ISupportServiceField {

    SearchTypeEnum searchType();

    JoinDirectionEnum joinDirection();

    boolean endJoin();

    default void where(SqlWhere where, String field, Object obj) {
        if (joinDirection() == JoinDirectionEnum.Or)
            where.or();
        else
            where.and();
        searchType().where(where, field, obj);
    }

}
