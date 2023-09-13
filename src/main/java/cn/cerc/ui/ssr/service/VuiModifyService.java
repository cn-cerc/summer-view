package cn.cerc.ui.ssr.service;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.ServiceException;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.client.ServiceExecuteException;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.service.VuiEntityMany.EntityManyOpenHelper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiModifyService extends VuiAbstractService<ISupportServiceHandler, ISupplierEntityOpen> {
    @Column
    String emptyMsg = "数据不存在，修改失败";

    private DataSet dataIn;

    public VuiModifyService() {
        super(ISupplierEntityOpen.class);
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        }
    }

    @Override
    public DataSet execute() throws ServiceException {
        Optional<ISupplierEntityOpen> entityHandler = binder.target();
        if (entityHandler.isPresent()) {
            ISupplierEntityOpen supplierEntityOpen = entityHandler.get();
            DataSet dataOut = new DataSet();
            if (supplierEntityOpen instanceof EntityManyOpenHelper && !dataIn.eof()) {
                for (DataRow row : dataIn) {
                    CustomEntity item = modify(supplierEntityOpen, row);
                    dataOut.append().current().loadFromEntity(item);
                }
            } else {
                CustomEntity item = modify(supplierEntityOpen, dataIn.head());
                dataOut.append().current().loadFromEntity(item);
            }
            return ISupportServiceDataOut.findDataOut(this, dataOut);
        }
        return new DataSet();
    }

    private CustomEntity modify(ISupplierEntityOpen supplierEntityOpen, DataRow row) throws ServiceExecuteException {
        return supplierEntityOpen.open().isEmptyThrow(() -> new ServiceExecuteException(emptyMsg)).update(item -> {
            Class<? extends CustomEntity> clazz = item.getClass();
            EntityHelper<? extends CustomEntity> helper = EntityHelper.get(clazz);
            binder.sendMessage(SsrMessage.InitDataIn, row);
            binder.sendMessage(SsrMessage.initEntityHelper, helper);
            binder.sendMessage(SsrMessage.RunServiceModify, item);
        }).get();
    }

    @Override
    public String getIdPrefix() {
        return "modify";
    }

}
