package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.BatchCache;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EntityMany;
import cn.cerc.mis.ado.EntityQuery;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiEntityQuery extends VuiContainer<ISupportServiceData>
        implements ISupportServiceHandler, IBinders, ISupplierEntityFields {
    private Binders binders = new Binders();
    private IHandle handle;
    private Class<? extends CustomEntity> entityClass;
    private BatchCache<? extends CustomEntity> findBatch;

    @Column
    String entityId = "";
    @Column
    Binder<VuiEntityQuery> joinMaster = new Binder<>(this, VuiEntityQuery.class);
    @Column
    String masterField = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitBinder:
            joinMaster.init();
            break;
        }
    }

    public DataSet queryMaster() {
        DataSet dataOut = EntityMany.open(handle, this.getEntityClass(), where -> {
            canvas().sendMessage(this, SsrMessage.initSqlWhere, new ServiceSqlWhere(where), null);
        }).dataSet().disableStorage();
        dataOut = ISupportServiceDataOut.findDataOut(this, dataOut);

        List<VuiEntityQuery> owners = binders.findOwners(VuiEntityQuery.class);
        for (VuiEntityQuery query : owners) {
            if (Utils.isEmpty(query.masterField))
                continue;
            String[] masterFields = query.masterField.split(",");
            for (DataRow row : dataOut) {
                String[] values = Arrays.stream(masterFields).map(row::getString).toArray(String[]::new);
                query.findBatchAndProcess(values).ifPresent(row::copyValues);
            }
        }
        return dataOut;
    }

    public Optional<DataRow> findBatchAndProcess(String... values) {
        if (findBatch == null)
            findBatch = EntityQuery.findBatch(handle, getEntityClass());
        Optional<? extends CustomEntity> optional = findBatch.get(values);
        if (optional.isEmpty())
            return Optional.empty();

        DataSet dataOut = new DataSet().append();
        dataOut.current().loadFromEntity(optional.get());
        DataRow row = ISupportServiceDataOut.findDataOut(this, dataOut).current();
        return Optional.ofNullable(row);
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
        return "entityQuery";
    }

}
