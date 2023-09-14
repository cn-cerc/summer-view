package cn.cerc.ui.ssr.service;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.EntityHelper;
import cn.cerc.db.core.ServiceException;
import cn.cerc.mis.ado.CustomEntity;
import cn.cerc.mis.client.ServiceExecuteException;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiModifyService extends VuiAbstractService<ISupportServiceHandler, ISupplierEntityOpen> {
    @Column
    String emptyMsg = "数据不存在，修改失败";
    @Column(name = "忽略数据不存在异常")
    boolean ignore;
    @Column(name = "是否为批量操作")
    boolean batch;

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
            if (batch && !dataIn.eof()) {
                while (dataIn.fetch()) {
                    CustomEntity item = modify(supplierEntityOpen);
                    dataOut.append().current().loadFromEntity(item);
                }
            } else {
                CustomEntity item = modify(supplierEntityOpen);
                dataOut.append().current().loadFromEntity(item);
            }
            return ISupportServiceDataOut.findDataOut(this, dataOut);
        }
        return new DataSet();
    }

    private CustomEntity modify(ISupplierEntityOpen supplierEntityOpen) throws ServiceExecuteException {
        VuiAbstractEntityOpenHelper<? extends CustomEntity> open = supplierEntityOpen.open();
        if (ignore && open.isEmpty())
            return null;
        return open.isEmptyThrow(() -> new RuntimeException(emptyMsg)).update(item -> {
            Class<? extends CustomEntity> clazz = item.getClass();
            EntityHelper<? extends CustomEntity> helper = EntityHelper.get(clazz);
            binder.sendMessage(SsrMessage.initEntityHelper, helper);
            binder.sendMessage(SsrMessage.RunServiceModify, item);
        }).get();
    }

    @Override
    public String getIdPrefix() {
        return "modify";
    }

}
