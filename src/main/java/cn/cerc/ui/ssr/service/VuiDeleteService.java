package cn.cerc.ui.ssr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.ServiceException;

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
            Optional<VuiSearchHeadIn> component = getComponent(VuiSearchHeadIn.class);
            List<ISupportFilter> filterList = new ArrayList<>();
            if (component.isPresent()) {
                component.get().validate();
                filterList = component.get().fields();
            }
            entityHandler.get().open(filterList).delete();
            return new DataSet();
        }
        return new DataSet();
    }

    @Override
    public String getIdPrefix() {
        return "delete";
    }

}