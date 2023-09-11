package cn.cerc.ui.ssr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.ServiceException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchService extends VuiAbstractService<ISupportServiceHandler, VuiEntityQuery> {

    @Column(name = "将结果输出到Head")
    boolean outputHead;

    public VuiSearchService() {
        super(VuiEntityQuery.class);
    }

    @Override
    public DataSet execute() throws ServiceException {
        Optional<VuiEntityQuery> entityQuery = binder.target();
        if (entityQuery.isPresent()) {
            Optional<VuiSearchHeadIn> component = getComponent(VuiSearchHeadIn.class);
            List<ISupportFilter> filterList = new ArrayList<>();
            if (component.isPresent()) {
                component.get().validate();
                filterList = component.get().fields();
            }
            DataSet dataOut = entityQuery.get().queryMaster(filterList);
            if (!dataOut.eof() && outputHead) {
                DataSet dataSet = new DataSet();
                dataSet.head().copyValues(dataOut.current());
                return dataSet;
            }
            return dataOut;
        }
        return new DataSet();
    }

    @Override
    public String getIdPrefix() {
        return "search";
    }

}
