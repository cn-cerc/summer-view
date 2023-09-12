package cn.cerc.ui.ssr.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.ServiceException;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.client.ServiceExecuteException;
import cn.cerc.ui.ssr.service.VuiEntityMany.EntityManyOpenHelper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiModifyService extends VuiAbstractService<ISupportServiceHandler, ISupplierEntityOpen> {

    private static final Logger log = LoggerFactory.getLogger(VuiModifyService.class);

    @Column
    String emptyMsg = "数据不存在，修改失败";

    public VuiModifyService() {
        super(ISupplierEntityOpen.class);
    }

    @Override
    public DataSet execute() throws ServiceException {
        Optional<ISupplierEntityOpen> entityHandler = binder.target();
        if (entityHandler.isPresent()) {
            ISupplierEntityOpen supplierEntityOpen = entityHandler.get();
            List<VuiModifyField> modifyFields = supplierEntityOpen.fields();
            if (supplierEntityOpen instanceof EntityManyOpenHelper && !dataIn.eof()) {
                Optional<VuiSearchBodyIn> component = getComponent(VuiSearchBodyIn.class);
                List<ISupportFilter> filterList = new ArrayList<>();
                if (component.isPresent()) {
                    component.get().validate();
                    filterList = component.get().fields();
                }
                for (DataRow row : dataIn) {
                    modify(supplierEntityOpen, filterList, modifyFields, row);
                }
            } else {
                Optional<VuiSearchHeadIn> component = getComponent(VuiSearchHeadIn.class);
                List<ISupportFilter> filterList = new ArrayList<>();
                if (component.isPresent()) {
                    component.get().validate();
                    filterList = component.get().fields();
                }
                modify(supplierEntityOpen, filterList, modifyFields, dataIn.head());
            }
            return new DataSet();
        }
        return new DataSet();
    }

    private void modify(ISupplierEntityOpen supplierEntityOpen, List<ISupportFilter> filterList,
            List<VuiModifyField> modifyFields, DataRow row) throws ServiceExecuteException {
        supplierEntityOpen.open(filterList).isEmptyThrow(() -> new ServiceExecuteException(emptyMsg)).update(item -> {
            Class<? extends CustomEntity> clazz = item.getClass();
            EntityHelper<? extends CustomEntity> helper = EntityHelper.get(clazz);
            Map<String, Field> fields = helper.fields();
            for (VuiModifyField modifyField : modifyFields) {
                String fieldCode = modifyField.field();
                if (!modifyField.required() && !row.hasValue(fieldCode))
                    continue;
                Field field = fields.get(fieldCode);
                if (field != null) {
                    try {
                        field.set(item, getByType(row, fieldCode, field.getType()));
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T getByType(DataRow row, String field, Class<T> clazz) {
        if (clazz == String.class)
            return (T) row.getString(field);
        else if (clazz == boolean.class || clazz == Boolean.class)
            return (T) Boolean.valueOf(row.getBoolean(field));
        else if (clazz == int.class || clazz == Integer.class)
            return (T) Integer.valueOf(row.getInt(field));
        else if (clazz == double.class || clazz == Double.class)
            return (T) Double.valueOf(row.getDouble(field));
        else if (clazz == long.class || clazz == Long.class)
            return (T) Long.valueOf(row.getLong(field));
        else if (clazz == Datetime.class)
            return (T) row.getDatetime(field);
        else if (clazz.isEnum())
            return (T) row.getEnum(field, (Class<Enum<?>>) clazz);
        else
            return (T) row.getValue(field);
    }

    @Override
    public String getIdPrefix() {
        return "modify";
    }

}
