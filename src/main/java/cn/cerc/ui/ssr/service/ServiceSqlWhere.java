package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.LinkOptionEnum;

public class ServiceSqlWhere implements IServiceSqlWhere {

    private SqlWhere where;

    public ServiceSqlWhere(SqlWhere where) {
        this.where = where;
    }

    @Override
    public ServiceSqlWhere eq(String field, Object obj) {
        where.eq(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere like(String field, Object obj, LinkOptionEnum link) {
        where.like(field, String.valueOf(obj), link);
        return this;
    }

    @Override
    public ServiceSqlWhere neq(String field, Object obj) {
        where.neq(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere lte(String field, Object obj) {
        where.lte(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere lt(String field, Object obj) {
        where.lt(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere gte(String field, Object obj) {
        where.gte(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere gt(String field, Object obj) {
        where.gt(field, obj);
        return this;
    }

    @Override
    public ServiceSqlWhere AND() {
        where = where.AND();
        return this;
    }

    @Override
    public ServiceSqlWhere and() {
        where.and();
        return this;
    }

    @Override
    public ServiceSqlWhere OR() {
        where = where.OR();
        return this;
    }

    @Override
    public ServiceSqlWhere or() {
        where.or();
        return this;
    }

    @Override
    public int size() {
        return where.size();
    }

}
