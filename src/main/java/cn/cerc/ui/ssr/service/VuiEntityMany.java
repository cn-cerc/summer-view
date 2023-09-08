package cn.cerc.ui.ssr.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EntityMany;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiEntityMany extends VuiContainer<VuiOutputField>
        implements ISupportServiceHandler, IBinders, ISupplierEntityOpen {
    private Binders binders = new Binders();
    private IHandle handle;
    private DataSet dataIn;
    private List<VuiModifyField> fields;

    @Column
    String entityId = "";
    @Column
    Binder<VuiEntityMany> joinMaster = new Binder<>(this, VuiEntityMany.class);
    @Column
    String masterField = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        case SsrMessage.InitBinder:
            joinMaster.init();
            break;
        }
    }

    @Override
    public AbstractEntityOpenHelper<? extends CustomEntity> open(List<ISupportFilter> filterList) {
        DataRow head = dataIn.head();
        EntityMany<? extends CustomEntity> many = EntityMany.open(handle, this.getEntityClass(), where -> {
            SqlWhere whereRef = where;
            for (ISupportFilter filter : filterList) {
                if (filter.endJoin())
                    whereRef = filter.joinDirection() == JoinDirectionEnum.And ? where.AND() : where.OR();
                String fieldCode = filter.field();
                Object obj = head.getValue(fieldCode);
                if (!filter.required() && head.hasValue(fieldCode))
                    filter.where(whereRef, fieldCode, obj);
                else if (filter.required())
                    filter.where(whereRef, fieldCode, obj);
            }
        });
        return new EntityManyOpenHelper<>(many);
    }

    private Class<? extends CustomEntity> getEntityClass() {
        var temp = entityId;
        var first = entityId.substring(0, 2);
        if (!first.toUpperCase().equals(first))
            temp = entityId.substring(0, 1).toLowerCase() + entityId.substring(1);
        return Application.getBean(temp, CustomEntity.class).getClass();
    }

    @Override
    public List<VuiModifyField> fields() {
        if (fields == null)
            fields = getFields(VuiModifyField.class);
        return fields;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getFields(Class<T> clazz) {
        return this.getComponents().stream().map(o -> {
            if (clazz.isInstance(o))
                return (T) o;
            return null;
        }).filter(Objects::nonNull).toList();
    }

    @Override
    public Binders binders() {
        return binders;
    }

    @Override
    public String getIdPrefix() {
        return "entityQuery";
    }

    public class EntityManyOpenHelper<T extends CustomEntity> extends AbstractEntityOpenHelper<T> {

        private EntityMany<T> entityMany;

        private EntityManyOpenHelper(EntityMany<T> entityMany) {
            this.entityMany = entityMany;
        }

        @Override
        public <X extends Throwable> AbstractEntityOpenHelper<T> isEmptyThrow(Supplier<? extends X> exceptionSupplier)
                throws X {
            entityMany.isEmptyThrow(exceptionSupplier);
            return this;
        }

        @Override
        public <X extends Throwable> AbstractEntityOpenHelper<T> isPresentThrow(Supplier<? extends X> exceptionSupplier)
                throws X {
            entityMany.isPresentThrow(exceptionSupplier);
            return this;
        }

        @Override
        public AbstractEntityOpenHelper<T> update(Consumer<T> action) {
            entityMany.updateAll(action);
            return null;
        }

        @Override
        public T insert(Consumer<T> action) {
            return entityMany.insert(action);
        }

        @Override
        public void delete() {
            entityMany.deleteAll();
        }

    }

}
