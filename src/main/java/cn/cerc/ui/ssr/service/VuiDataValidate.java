package cn.cerc.ui.ssr.service;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.mis.math.FunctionField;
import cn.cerc.mis.math.FunctionIf;
import cn.cerc.mis.math.FunctionManager;
import cn.cerc.mis.math.FunctionMath;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiDataValidate extends VuiComponent implements ISupportDataValidate {

    private DataSet dataIn;

    @Column
    Binder<VuiAbstractService> binder = new Binder<>(this, VuiAbstractService.class);
    @Column
    String validate = "";
    @Column
    String message = "数据校验不通过";
    @Column
    boolean isBody = false;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        }
    }

    @Override
    public void validate() throws DataValidateException {
        if (Utils.isEmpty(validate))
            return;
        FunctionManager manager = new FunctionManager();
        manager.addFunction(new FunctionIf());
        manager.addFunction(new FunctionMath());
        manager.addFunction(new FunctionField(dataIn.head()));
        boolean result = manager.parse(String.format("if(%s,true,false)", validate)).getBoolean();
        DataValidateException.stopRun(message, result);
    }

    @Override
    public String getIdPrefix() {
        return "validate";
    }

    public static void main(String[] args) {
        DataSet dataSet = new DataSet();
        dataSet.head().setValue("Code_", "");
        System.out.println(dataSet.json());
    }

}
