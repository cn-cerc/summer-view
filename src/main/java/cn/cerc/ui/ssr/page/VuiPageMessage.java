package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.db.redis.Redis;
import cn.cerc.ui.mvc.AbstractPage;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

/**
 * 用于监听页面服务发送的消息并将消息放入Redis中，达成页面跳转传递消息
 * 
 * 需保证两个页面的key相同
 * 
 * ${CorpNo} handle.getCorpNo()
 * 
 * ${UserCode} handle.getUserCode()
 * 
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("页面消息")
public class VuiPageMessage extends VuiComponent implements ISupportCanvas {

    private IHandle handle;

    @Column
    String key = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitPage:
            if (msgData instanceof AbstractPage page) {
                try (Redis redis = new Redis()) {
                    String msgKey = buildKey();
                    String msg = redis.get(msgKey);
                    if (!Utils.isEmpty(msg)) {
                        page.setMessage(msg);
                        redis.del(msgKey);
                    }
                }
            }
            break;
        case SsrMessage.SuccessOnService:
            if (msgData instanceof String msg) {
                try (Redis redis = new Redis()) {
                    redis.setex(buildKey(), 60, msg);
                }
            }
            break;
        }
    }

    private String buildKey() {
        SsrBlock block = new SsrBlock(key);
        block.option("CorpNo", handle.getCorpNo());
        block.option("UserCode", handle.getUserCode());
        return block.html();
    }

    @Override
    public String getIdPrefix() {
        return "message";
    }

}
