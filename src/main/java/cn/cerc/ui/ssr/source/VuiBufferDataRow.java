package cn.cerc.ui.ssr.source;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.other.MemoryBuffer;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiBufferType;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.report.ISupportRpt;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("缓存数据行")
public class VuiBufferDataRow extends VuiComponent implements ICommonSupplierDataRow, ISupportXls, ISupportRpt, IBinders {
    private static final Logger log = LoggerFactory.getLogger(VuiBufferDataRow.class);
    private IHandle handle;
    private Binders binders = new Binders();

    private MemoryBuffer buffer;
    @Column
    String bufferKey = "";
    @Column
    VuiBufferType bufferType = null;

    @Override
    public DataRow dataRow() {
        return buffer.getRecord();
    }

    @Override
    public String getIdPrefix() {
        return "dataRow";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitProperties:
            if (bufferType == null) {
                log.error("bufferType 不允许为空！");
                return;
            }
            if (!Utils.isEmpty(bufferKey) && buffer == null) {
                SsrBlock block = new SsrBlock(bufferKey);
                block.option("CorpNo", handle.getCorpNo());
                block.option("UserCode", handle.getUserCode());
                buffer = new MemoryBuffer(bufferType, block.html());
                canvas().sendMessage(this, SsrMessage.RefreshProperties, buffer.getRecord(), null);
            }
            break;
        }
    }

    @Override
    public Binders binders() {
        return binders;
    }

}
