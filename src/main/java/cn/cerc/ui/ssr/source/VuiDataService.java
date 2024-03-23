package cn.cerc.ui.ssr.source;

import java.lang.reflect.Field;
import java.util.List;
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
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;
import cn.cerc.mis.client.ServiceSign;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.CustomEntityService;
import cn.cerc.mis.core.EntityServiceField;
import cn.cerc.mis.core.IEntityServiceFields;
import cn.cerc.mis.core.IService;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.EntityServiceRecord;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.report.ISupportRpt;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("数据服务")
@VuiCommonComponent
public class VuiDataService extends VuiComponent implements ISupplierDataRow, ISupportCanvas, ISupportChart,
        ISupplierFields, IBinders, ISupplierDataSet, ISupportXls, ISupportRpt {
    private static final Logger log = LoggerFactory.getLogger(VuiDataService.class);
    private DataSet dataSet = new DataSet();
    private Binders binders = new Binders();
    protected IHandle handle;
    @Column
    EntityServiceRecord service = EntityServiceRecord.EMPTY;
    @Column(name = "成功后发送消息")
    String success_message = "";
    @Column
    Binder<ISupplierDataRow> headIn = new Binder<>(this, ISupplierDataRow.class);
    @Column
    Binder<ISupplierDataSet> bodyIn = new Binder<>(this, ISupplierDataSet.class);
    @Column(name = "在启动时立即执行")
    boolean callByInit = false;

    public Binder<ISupplierDataRow> headIn() {
        return this.headIn;
    }

    public Binder<ISupplierDataSet> bodyIn() {
        return this.bodyIn;
    }

    public void service(String service) {
        this.service = new EntityServiceRecord(service, service);
    }

    public void service(String service, String serviceDesc) {
        this.service = new EntityServiceRecord(service, serviceDesc);
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
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitBinder:
            this.headIn.init();
            this.bodyIn.init();
            break;
        case SsrMessage.InitContent: {
            if (this.callByInit) {
                if (!Utils.isEmpty(this.service.service())) {
                    Optional<ISupplierDataRow> target = this.headIn.target();
                    // 如果绑定的数据源是VuiForm，那么就需要等待VuiForm执行InitContent后发送InitContent消息才执行
                    if (target.isPresent() && target.get() instanceof VuiForm && !(sender instanceof VuiForm))
                        break;
                    DataSet dataIn = dataIn();
                    ServiceSign svr = callService(dataIn);
                    if (svr.isFail()) {
                        this.canvas().sendMessage(this, SsrMessage.FailOnService, svr.message(), null);
                        binders.sendMessage(this, SsrMessage.FailOnService, svr.message(), null);
                        break;
                    }
                    this.dataSet = svr.dataOut();
                    this.canvas().sendMessage(this, SsrMessage.RefreshProperties, dataSet, null);
                    binders.sendMessage(this, SsrMessage.SuccessOnService, null, null);
                    if (!Utils.isEmpty(this.success_message))
                        this.canvas().sendMessage(this, SsrMessage.SuccessOnService, this.success_message, null);
                }
            }
            break;
        }
        case SsrMessage.AfterSubmit: {
            if (handle == null) // 非运行环境
                break;
            var target = this.headIn.target();
            if (target.isPresent()) {
                DataSet dataIn = dataIn();
                ServiceSign svr = callService(dataIn);
                if (svr.isFail()) {
                    binders.sendMessage(this, SsrMessage.FailOnService, svr.message(), null);
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

    public DataSet dataIn() {
        DataSet dataIn = new DataSet();
        this.headIn.target().ifPresent(headIn -> dataIn.head().copyValues(headIn.dataRow()));
        this.bodyIn.target().ifPresent(bodyIn -> dataIn.appendDataSet(bodyIn.dataSet()));
        return dataIn;
    }

    protected ServiceSign callService(DataSet dataIn) {
        return new ServiceSign(this.service.service()).callLocal(handle, dataIn);
    }

    @Override
    public DataRow dataRow() {
        if (dataSet.size() > 1)
            log.warn("service {} 返回的记录有多笔", this.service);
        if (dataSet.size() > 0) {
            dataSet.first();
            return dataSet.current();
        }
        return new DataRow();
    }

    @Override
    public List<EntityServiceField> fields(int fieldsType) {
        if (handle == null)
            return List.of();
        if (Utils.isEmpty(this.service()))
            return List.of();
        try {
            IService svr = Application.getService(handle, this.service(), new Variant());
            if (svr instanceof CustomEntityService<?, ?, ?, ?> service) {
                Set<Field> fields = switch (fieldsType) {
                case HeadInFields -> service.getMetaHeadIn().keySet();
                case BodyInFields -> service.getMetaBodyIn().keySet();
                case HeadOutFields -> service.getMetaHeadOut().keySet();
                case BodyOutFields -> service.getMetaBodyOut().keySet();
                default -> Set.of();
                };
                return fields.stream().map(EntityServiceField::new).toList();
            } else if (svr instanceof IEntityServiceFields service) {
                return switch (fieldsType) {
                case HeadInFields -> service.getHeadInFields();
                case BodyInFields -> service.getBodyInFields();
                case HeadOutFields -> service.getHeadOutFields();
                case BodyOutFields -> service.getBodyOutFields();
                default -> List.of();
                };
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Binders binders() {
        return binders;
    }

    protected String successMessage() {
        return success_message;
    }

    public boolean callByInit() {
        return callByInit;
    }

    public VuiDataService callByInit(boolean callByInit) {
        this.callByInit = callByInit;
        return this;
    }

}
