package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EntityMany;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiEntityMany extends VuiContainer<ISupportServiceData>
        implements ISupportServiceHandler, IBinders, ISupplierEntityOpen {
    private Binders binders = new Binders();
    private IHandle handle;
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
        case SsrMessage.initEntityHelper, SsrMessage.RunServiceModify:
            if (getOwner() == sender) {
                for (UIComponent component : this.getComponents()) {
                    if (component instanceof VuiComponent vuiComponent && component instanceof ISupportModifyDataIn) {
                        vuiComponent.onMessage(this, msgType, msgData, null);
                    }
                }
            }
            break;
        }
    }

    @Override
    public VuiAbstractEntityOpenHelper<? extends CustomEntity> open() {
        EntityMany<? extends CustomEntity> many = EntityMany.open(handle, this.getEntityClass(), where -> {
            canvas().sendMessage(this, SsrMessage.initSqlWhere, new ServiceSqlWhere(where), null);
        });
        return new EntityManyOpenHelper<>(many);
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
    public Binders binders() {
        return binders;
    }

    @Override
    public String getIdPrefix() {
        return "entityMany";
    }

    public class EntityManyOpenHelper<T extends CustomEntity> extends VuiAbstractEntityOpenHelper<T> {

        private EntityMany<T> entityMany;

        private EntityManyOpenHelper(EntityMany<T> entityMany) {
            this.entityMany = entityMany;
        }

        @Override
        public boolean isEmpty() {
            return entityMany.isEmpty();
        }

        @Override
        public <X extends Throwable> VuiAbstractEntityOpenHelper<T> isEmptyThrow(
                Supplier<? extends X> exceptionSupplier) throws X {
            entityMany.isEmptyThrow(exceptionSupplier);
            return this;
        }

        @Override
        public boolean isPresent() {
            return entityMany.isPresent();
        }

        @Override
        public <X extends Throwable> VuiAbstractEntityOpenHelper<T> isPresentThrow(
                Supplier<? extends X> exceptionSupplier) throws X {
            entityMany.isPresentThrow(exceptionSupplier);
            return this;
        }

        @Override
        public VuiAbstractEntityOpenHelper<T> update(Consumer<T> action) {
            entityMany.updateAll(action);
            return this;
        }

        @Override
        public T insert(Consumer<T> action) {
            return entityMany.insert(action);
        }

        @Override
        public T delete() {
            entityMany.deleteAll();
            return null;
        }

        @Override
        public T get() {
            if (entityMany.isEmpty() || entityMany.size() == 0)
                return null;
            return entityMany.get(0);
        }

    }

}
