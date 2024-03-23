package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere.LinkOptionEnum;

public interface IServiceSqlWhere {

    public IServiceSqlWhere eq(String field, Object obj);

    public IServiceSqlWhere like(String field, Object obj, LinkOptionEnum link);

    public IServiceSqlWhere neq(String field, Object obj);

    public IServiceSqlWhere lte(String field, Object obj);

    public IServiceSqlWhere lt(String field, Object obj);

    public IServiceSqlWhere gte(String field, Object obj);

    public IServiceSqlWhere gt(String field, Object obj);

    public IServiceSqlWhere AND();

    public IServiceSqlWhere and();

    public IServiceSqlWhere OR();

    public IServiceSqlWhere or();

    public int size();

}
