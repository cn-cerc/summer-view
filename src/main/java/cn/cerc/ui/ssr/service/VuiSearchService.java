package cn.cerc.ui.ssr.service;

import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.ServiceException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchService extends VuiAbstractService<ISupportServiceHandler, VuiEntityQuery> {

    public VuiSearchService() {
        super(VuiEntityQuery.class);
    }

    @Override
    public DataSet execute() throws ServiceException {
        Optional<VuiEntityQuery> entityQuery = binder.target();
        if (entityQuery.isPresent())
            return entityQuery.get().queryMaster();
        return new DataSet();
    }

    @Override
    public String getIdPrefix() {
        return "search";
    }

}
