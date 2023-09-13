package cn.cerc.ui.ssr.service;

import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.ServiceException;
import cn.cerc.mis.ado.CustomEntity;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiDeleteService extends VuiAbstractService<ISupportServiceHandler, ISupplierEntityOpen> {

    public VuiDeleteService() {
        super(ISupplierEntityOpen.class);
    }

    @Override
    public DataSet execute() throws ServiceException {
        Optional<ISupplierEntityOpen> entityHandler = binder.target();
        if (entityHandler.isPresent()) {
            CustomEntity entity = entityHandler.get().open().delete();
            if (entity != null) {
                DataSet dataOut = new DataSet();
                dataOut.append().current().loadFromEntity(entity);
                return ISupportServiceDataOut.findDataOut(this, dataOut);
            }
            return new DataSet();
        }
        return new DataSet();
    }

    @Override
    public String getIdPrefix() {
        return "delete";
    }

}
