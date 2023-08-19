package cn.cerc.ui.ssr.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.client.ServiceSign;
import cn.cerc.mis.core.CustomEntityService;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.core.SsrUtils;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.SsrDataRowSourceImpl;
import cn.cerc.ui.ssr.page.ISupportVisualContainer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("数据服务")
public class SsrDataService extends SsrComponent
        implements SsrDataRowSourceImpl, ISupplierFields, ISupportVisualContainer, IBinders {
    private static final Logger log = LoggerFactory.getLogger(SsrDataService.class);
    private DataSet dataSet = new DataSet();
    private IPage page;
    private Binders binders = new Binders();
    @Column
    String service = "";
    @Column(name = "成功后发送消息")
    String success_message = "";
    @Column
    Binder<SsrDataRowSourceImpl> headIn = new Binder<>(SsrDataRowSourceImpl.class);
    @Column(name = "在启动时立即执行")
    boolean callByInit = false;

    public Binder<SsrDataRowSourceImpl> headIn() {
        return this.headIn;
    }

    public SsrDataService() {
        super();
        this.headIn.owner(this);
    }

    public void service(String service) {
        this.service = service;
    }

    public String service() {
        return this.service;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String getIdPrefix() {
        return "service";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitPage:
            if (msgData instanceof IPage page)
                this.page = page;
            break;
        case SsrMessage.InitContent: {
            if (this.callByInit) {
                if (!Utils.isEmpty(this.service)) {
                    var dataIn = new DataRow();
                    var target = this.headIn.target();
                    if (target.isPresent())
                        dataIn = target.get().dataRow();
                    var svr = new ServiceSign(this.service).callLocal(page.getForm(), dataIn);
                    if (svr.isFail())
                        throw new RuntimeException(svr.message());
                    this.dataSet = svr.dataOut();
                    this.getContainer().sendMessage(this, SsrMessage.RefreshProperties, dataSet, null);
                    binders.sendMessage(this, SsrMessage.SuccessOnService, null, null);
                }
            }
            break;
        }
        case SsrMessage.InitBinder:
            this.headIn.init();
            break;
        case SsrMessage.AfterSubmit: {
            if (page == null) // 非运行环境
                break;
            if (!this.callByInit) {
                var target = this.headIn.target();
                if (target.isPresent()) {
                    var svr = new ServiceSign(this.service).callLocal(page.getForm(), target.get().dataRow());
                    if (svr.isFail()) {
                        binders.sendMessage(this, SsrMessage.FailOnService, null, null);
                        this.getContainer().sendMessage(this, SsrMessage.FailOnService, svr.message(), null);
                        return;
                    } else {
                        this.dataSet = svr.dataOut();
                        binders.sendMessage(this, SsrMessage.SuccessOnService, null, null);
                    }
                    if (!Utils.isEmpty(this.success_message))
                        this.getContainer()
                                .sendMessage(this, SsrMessage.SuccessOnService, this.success_message, null);
                    else if (!Utils.isEmpty(svr.message()))
                        this.getContainer().sendMessage(this, SsrMessage.SuccessOnService, svr.message(), null);
                } else {
                    log.warn("找不到绑定对象：{}", this.headIn.targetId());
                }
            }
        }
            break;
        }
    }

    @Override
    public DataRow dataRow() {
        if (dataSet.size() > 0) {
            log.error("service {} 返回的记录有多笔", this.service);
            dataSet.first();
            return dataSet.current();
        }
        return new DataRow();
    }

    @Override
    public List<Field> fields(int fieldsType) {
        var optBean = SsrUtils.getBean(this.service, CustomEntityService.class);
        if (optBean.isPresent()) {
            CustomEntityService<?, ?, ?, ?> svr = optBean.get();
            switch (fieldsType) {
            case HeadInFields:
                return svr.getMetaHeadIn();
            case BodyOutFields:
                return svr.getMetaBodyOut();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Binders binders() {
        return binders;
    }

}
