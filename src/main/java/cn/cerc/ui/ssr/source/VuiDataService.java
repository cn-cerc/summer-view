package cn.cerc.ui.ssr.source;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

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
import cn.cerc.ui.ssr.core.EntityServiceRecord;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("数据服务")
public class VuiDataService extends VuiComponent
        implements ICommonSupplierDataRow, ISupplierFields, IBinders, ICommonSupplierDataSet {
    private static final Logger log = LoggerFactory.getLogger(VuiDataService.class);
    private DataSet dataSet = new DataSet();
    private IPage page;
    private Binders binders = new Binders();
    @Column
    EntityServiceRecord service = EntityServiceRecord.EMPTY;
    @Column(name = "成功后发送消息")
    String success_message = "";
    @Column
    Binder<ISupplierDataRow> headIn = new Binder<>(this, ISupplierDataRow.class);
    @Column(name = "在启动时立即执行")
    boolean callByInit = false;

    public Binder<ISupplierDataRow> headIn() {
        return this.headIn;
    }

    public void service(String service) {
        this.service = new EntityServiceRecord(service, service);
    }

    public String service() {
        return this.service.service();
    }

    public String serviceDesc() {
        return this.service.desc();
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    public void dataSet(DataSet dataSet) {
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
        case SsrMessage.InitBinder:
            this.headIn.init();
            break;
        case SsrMessage.InitContent: {
            if (this.callByInit) {
                if (!Utils.isEmpty(this.service.service())) {
                    DataRow dataIn = new DataRow();
                    Optional<ISupplierDataRow> target = this.headIn.target();
                    if (target.isPresent()) {
                        // 如果绑定的数据源是VuiForm，那么就需要等待VuiForm执行InitContent后发送InitContent消息才执行
                        if (target.get() instanceof VuiForm && !(sender instanceof VuiForm))
                            break;
                        dataIn = target.get().dataRow();
                    }
                    var svr = new ServiceSign(this.service.service()).callLocal(page.getForm(), dataIn);
                    if (svr.isFail())
                        throw new RuntimeException(svr.message());
                    this.dataSet = svr.dataOut();
                    this.canvas().sendMessage(this, SsrMessage.RefreshProperties, dataSet, null);
                    binders.sendMessage(this, SsrMessage.SuccessOnService, null, null);
                }
            }
            break;
        }
        case SsrMessage.AfterSubmit: {
            if (page == null) // 非运行环境
                break;
            var target = this.headIn.target();
            if (target.isPresent()) {
                var svr = new ServiceSign(this.service.service()).callLocal(page.getForm(), target.get().dataRow());
                if (svr.isFail()) {
                    binders.sendMessage(this, SsrMessage.FailOnService, null, null);
                    this.canvas().sendMessage(this, SsrMessage.FailOnService, svr.message(), null);
                    return;
                } else {
                    this.dataSet = svr.dataOut();
                    binders.sendMessage(this, SsrMessage.SuccessOnService, null, null);
                }
                if (!Utils.isEmpty(this.success_message))
                    this.canvas().sendMessage(this, SsrMessage.SuccessOnService, this.success_message, null);
                else if (!Utils.isEmpty(svr.message()))
                    this.canvas().sendMessage(this, SsrMessage.SuccessOnService, svr.message(), null);
            } else {
                log.warn("找不到绑定对象：{}", this.headIn.targetId());
            }
            break;
        }
        }
    }

    @Override
    public DataRow dataRow() {
        if (dataSet.size() > 1)
            log.error("service {} 返回的记录有多笔", this.service);
        if (dataSet.size() > 0) {
            dataSet.first();
            return dataSet.current();
        }
        return new DataRow();
    }

    @Override
    public Set<Field> fields(int fieldsType) {
        IVuiEnvironment environment = this.canvas().environment();
        if (Utils.isEmpty(this.service.service()))
            return new LinkedHashSet<>();
        var optBean = environment.getBean(this.service.service(), CustomEntityService.class);
        if (optBean.isPresent()) {
            CustomEntityService<?, ?, ?, ?> svr = optBean.get();
            switch (fieldsType) {
            case HeadOutFields:
                return svr.getMetaHeadOut().keySet();
            case HeadInFields:
                return svr.getMetaHeadIn().keySet();
            case BodyInFields:
                return svr.getMetaBodyIn().keySet();
            case BodyOutFields:
                return svr.getMetaBodyOut().keySet();
            }
        }
        return new LinkedHashSet<>();
    }

    @Override
    public Binders binders() {
        return binders;
    }

}
