package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchField extends VuiControl implements ISupportFilter {
    private DataRow dataIn;

    @Column
    String field = "";
    @Column
    String title = "";
    @Column
    boolean required = false;
    @Column
    SearchTypeEnum searchType = SearchTypeEnum.EQ;
    @Column(name = "在当前字段结束前面的组合")
    boolean endJoin = false;
    @Column(name = "条件组合")
    JoinDirectionEnum joinEnum = JoinDirectionEnum.And;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (getOwner() != sender)
                return;
            if (msgData instanceof DataRow dataIn)
                this.dataIn = dataIn;
            break;
        case SsrMessage.initSqlWhere:
            if (getOwner() != sender)
                return;
            if (dataIn == null)
                throw new RuntimeException("DataIn为NULL！");
            if (msgData instanceof ServiceSqlWhere sqlWhere) {
                String fieldCode = this.field();
                String title = Utils.isEmpty(this.title()) ? this.field() : this.title();
                if (this.required() && !dataIn.hasValue(fieldCode))
                    throw new RuntimeException(String.format("%s不允许为空", title));
                if (!this.required() && !dataIn.hasValue(fieldCode))
                    return;

                SqlWhere where = sqlWhere.where();
                if (this.endJoin() && where.size() > 0)
                    sqlWhere.where(this.joinDirection() == JoinDirectionEnum.And ? where.AND() : where.OR());
                Object obj = dataIn.getValue(fieldCode);
                this.where(sqlWhere.where(), fieldCode, obj);
            }
        }
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public SearchTypeEnum searchType() {
        return searchType;
    }

    @Override
    public JoinDirectionEnum joinDirection() {
        return joinEnum;
    }

    @Override
    public boolean endJoin() {
        return endJoin;
    }

    @Override
    public void field(String field) {
        this.field = field;
    }

    @Override
    public void title(String title) {
        this.title = title;
    }

}
