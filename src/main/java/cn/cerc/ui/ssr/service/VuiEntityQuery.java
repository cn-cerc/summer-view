package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.FieldDefs;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.SqlWhere;
import cn.cerc.db.core.SqlWhere.JoinDirectionEnum;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.ado.EntityMany;
import cn.cerc.mis.ado.EntityQuery;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiEntityQuery extends VuiAbstractEntityContainer<VuiOutputField>
        implements ISupportServiceHandler, IBinders {
    private Binders binders = new Binders();
    private IHandle handle;
    private DataSet dataIn;
    private Set<String> outputFields;

    @Column
    String entityId = "";
    @Column
    Binder<VuiEntityQuery> joinMaster = new Binder<>(this, VuiEntityQuery.class);
    @Column
    String masterField = "";
    private Class<? extends CustomEntity> entityClass;

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
        case SsrMessage.InitBinder:
            joinMaster.init();
            break;
        }
    }

    public DataSet queryMaster(List<ISupportFilter> filterList) {
        DataRow head = dataIn.head();

        DataSet dataOut = EntityMany.open(handle, this.getEntityClass(), where -> {
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
        }).dataSet().disableStorage();

        Set<String> outputFields = getOutputFields();
        if (!outputFields.contains("*")) {
            FieldDefs fields = dataOut.fields();
            List<String> removeFields = fields.getItems()
                    .stream()
                    .map(FieldMeta::code)
                    .filter(t -> !outputFields.contains(t))
                    .toList();
            for (String field : removeFields) {
                fields.remove(field);
            }
        }

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
        Optional<? extends CustomEntity> optional = EntityQuery.findBatch(handle, getEntityClass()).get(values);
        if (optional.isEmpty())
            return Optional.empty();

        DataRow row = new DataRow().loadFromEntity(optional.get());
        Set<String> outputFields = getOutputFields();
        if (!outputFields.contains("*")) {
            FieldDefs fields = row.fields();
            List<String> removeFields = fields.getItems()
                    .stream()
                    .map(FieldMeta::code)
                    .filter(t -> !outputFields.contains(t))
                    .toList();
            for (String field : removeFields) {
                fields.remove(field);
            }
        }
        return Optional.of(row);
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

    public Set<String> getOutputFields() {
        if (outputFields == null) {
            outputFields = this.getComponents().stream().map(o -> {
                if (o instanceof VuiOutputField field)
                    return field;
                return null;
            }).filter(Objects::nonNull).map(VuiOutputField::field).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return outputFields;
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
