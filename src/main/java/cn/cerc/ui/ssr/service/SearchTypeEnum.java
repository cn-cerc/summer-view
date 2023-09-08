package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.LinkOptionEnum;

public enum SearchTypeEnum {
    EQ,
    NEQ,
    GT,
    GTE,
    LT,
    LTE,
    LIKE;

    public void where(SqlWhere where, String field, Object obj) {
        switch (this) {
        case EQ -> where.eq(field, obj);
        case NEQ -> where.neq(field, obj);
        case GT -> where.gt(field, obj);
        case GTE -> where.gte(field, obj);
        case LT -> where.lt(field, obj);
        case LTE -> where.lte(field, obj);
        case LIKE -> where.like(field, String.valueOf(obj), LinkOptionEnum.All);
        }
    }

}
