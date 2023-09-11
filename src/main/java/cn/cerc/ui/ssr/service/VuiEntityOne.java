package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EntityOne;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiEntityOne extends VuiAbstractEntityContainer<VuiModifyField>
        implements ISupportServiceHandler, IBinders, ISupplierEntityOpen {
    private Binders binders = new Binders();
    private IHandle handle;
    private DataSet dataIn;
    private List<VuiModifyField> fields;
    private Class<? extends CustomEntity> entityClass;

    @Column
    String entityId = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        }
    }

    @Override
    public VuiAbstractEntityOpenHelper<? extends CustomEntity> open(List<ISupportFilter> filterList) {
        DataRow head = dataIn.head();
        EntityOne<? extends CustomEntity> one = EntityOne.open(handle, this.getEntityClass(), where -> {
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
        return new EntityOneOpenHelper<>(one);
    }

    @Override
    public Set<Field> entityFields() {
        Class<? extends CustomEntity> entityClass = getEntityClass();
        if (entityClass == null)
            return Set.of();
        return new LinkedHashSet<>(EntityHelper.get(entityClass).fields().values());
    }

    private Class<? extends CustomEntity> getEntityClass() {
        if (Utils.isEmpty(entityId))
            return null;
        if (entityClass == null) {
            var temp = entityId;
            var first = entityId.substring(0, 2);
            if (!first.toUpperCase().equals(first))
                temp = entityId.substring(0, 1).toLowerCase() + entityId.substring(1);
            entityClass = Application.getBean(temp, CustomEntity.class).getClass();
            return entityClass;
        }
        return entityClass;
    }

    @Override
    public List<VuiModifyField> fields() {
        if (fields == null) {
            fields = this.getComponents().stream().map(o -> {
                if (o instanceof VuiModifyField field)
                    return field;
                return null;
            }).filter(Objects::nonNull).toList();
        }
        return fields;
    }

    @Override
    public Binders binders() {
        return binders;
    }

    @Override
    public String getIdPrefix() {
        return "entityOne";
    }

    public class EntityOneOpenHelper<T extends CustomEntity> extends VuiAbstractEntityOpenHelper<T> {

        private EntityOne<T> entityOne;

        private EntityOneOpenHelper(EntityOne<T> entityOne) {
            this.entityOne = entityOne;
        }

        @Override
        public <X extends Throwable> VuiAbstractEntityOpenHelper<T> isEmptyThrow(
                Supplier<? extends X> exceptionSupplier) throws X {
            entityOne.isEmptyThrow(exceptionSupplier);
            return this;
        }

        @Override
        public <X extends Throwable> VuiAbstractEntityOpenHelper<T> isPresentThrow(
                Supplier<? extends X> exceptionSupplier) throws X {
            entityOne.isPresentThrow(exceptionSupplier);
            return this;
        }

        @Override
        public EntityOneOpenHelper<T> update(Consumer<T> action) {
            entityOne.update(action);
            return this;
        }

        @Override
        public T insert(Consumer<T> action) {
            return entityOne.orElseInsert(action);
        }

        @Override
        public void delete() {
            entityOne.delete();
        }

    }

}
