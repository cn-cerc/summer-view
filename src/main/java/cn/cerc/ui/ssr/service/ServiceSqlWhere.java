package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.SqlWhere;

public class ServiceSqlWhere {

    private SqlWhere where;

    public ServiceSqlWhere(SqlWhere where) {
        this.where = where;
    }

    public SqlWhere where() {
        return this.where;
    }

    public ServiceSqlWhere where(SqlWhere where) {
        this.where = where;
        return this;
    }

}
