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

    @SuppressWarnings("unchecked")
    private void modify(ISupplierEntityOpen supplierEntityOpen, List<ISupportFilter> filterList,
            List<VuiModifyField> modifyFields, DataRow row) throws ServiceExecuteException {
        supplierEntityOpen.open(filterList).isEmptyThrow(() -> new ServiceExecuteException(emptyMsg)).update(item -> {
            Class<? extends CustomEntity> clazz = item.getClass();
            EntityHelper<? extends CustomEntity> helper = EntityHelper.get(clazz);
            Map<String, Field> fields = helper.fields();
            for (VuiModifyField modifyField : modifyFields) {
                String fieldCode = modifyField.field();
                Object obj = row.getValue(fieldCode);
                if (!modifyField.required() && !row.hasValue(fieldCode))
                    continue;
                Field field = fields.get(fieldCode);
                if (field != null) {
                    try {
                        if (field.getType().isEnum())
                            field.set(item, row.getEnum(fieldCode, (Class<Enum<?>>) field.getType()));
                        else
                            field.set(item, obj);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    @Override
    public String getIdPrefix() {
        return "modify";
    }

}
