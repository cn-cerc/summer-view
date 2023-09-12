package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;
import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchField extends VuiControl implements ISupportFilter {

    @Column
    String field = "";
    @Column
    String alias = "";
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

    @Override
    public void required(boolean required) {
        this.required = required;
    }

}
