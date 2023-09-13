package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ServiceException;
import cn.cerc.mis.core.JsonPage;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;

public abstract class VuiAbstractService<T extends ISupportServiceHandler, B> extends VuiContainer<T>
        implements ISupportService, IBinders {

    protected Binders binders = new Binders();

    protected ServiceExceptionHandler exceptionHandler;

    protected JsonPage page;

    protected IHandle handle;

    public abstract DataSet execute() throws ServiceException;

    @Column
    Binder<B> binder;

    public VuiAbstractService(Class<B> binderClass) {
        this.binder = new Binder<>(this, binderClass);
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitPage:
            if (msgData instanceof JsonPage page)
                this.page = page;
            break;
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.InitExceptionHandler:
            if (msgData instanceof ServiceExceptionHandler handler)
                this.exceptionHandler = handler;
            break;
        case SsrMessage.InitContent:
            try {
                DataSet dataSet = this.execute();
                page.setData(dataSet);
            } catch (Exception e) {
                exceptionHandler.setException(e);
            }
            break;
        }
    }

    @Override
    public String getIdPrefix() {
        return "service";
    }

    @Override
    public Binders binders() {
        return this.binders;
    }

}
