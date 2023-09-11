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
public class VuiAppendService extends VuiAbstractService<ISupportServiceHandler, ISupplierEntityOpen> {

    private static final Logger log = LoggerFactory.getLogger(VuiAppendService.class);

    @Column
    String presentMsg = "数据已存在，增加失败";

    public VuiAppendService() {
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
                    insert(supplierEntityOpen, filterList, modifyFields, row);
                }
            } else {
                Optional<VuiSearchHeadIn> component = getComponent(VuiSearchHeadIn.class);
                List<ISupportFilter> filterList = new ArrayList<>();
                if (component.isPresent()) {
                    component.get().validate();
                    filterList = component.get().fields();
                }
                insert(supplierEntityOpen, filterList, modifyFields, dataIn.head());
            }
            return new DataSet();
        }
        return new DataSet();
    }

    @SuppressWarnings("unchecked")
    public void insert(ISupplierEntityOpen supplierEntityOpen, List<ISupportFilter> filterList,
            List<VuiModifyField> modifyFields, DataRow row) throws ServiceExecuteException {
        supplierEntityOpen.open(filterList)
                .isPresentThrow(() -> new ServiceExecuteException(presentMsg))
                .insert(item -> {
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
        return "append";
    }

}
