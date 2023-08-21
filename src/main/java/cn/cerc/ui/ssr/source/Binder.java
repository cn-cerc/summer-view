package cn.cerc.ui.ssr.source;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiComponent;

public class Binder<T> {
    private static final Logger log = LoggerFactory.getLogger(Binder.class);
    private VuiComponent owner;
    private T target;
    private String targetId = "";
    private Class<T> targetType;

    public Binder(VuiComponent owner, Class<T> targetType) {
        this.targetType = targetType;
        this.owner = owner;
    }

    public VuiComponent owner() {
        return owner;
    }

    public Class<T> targetType() {
        return this.targetType;
    }

    public void targetId(String targetId) {
        this.targetId = targetId;
    }

    public String targetId() {
        return this.targetId;
    }

    public Optional<T> target() {
        if (target == null)
            init();
        return Optional.ofNullable(target);
    }

    public void init() {
        if (target != null)
            return;
        if (!Utils.isEmpty(this.targetId)) {
            var temp = owner.canvas().getMember(targetId, targetType());
            if (temp.isPresent()) {
                this.target = temp.get();
                if (target instanceof IBinders impl)
                    impl.binders().add(this);
            } else {
                log.warn("{} 绑定的对象 {} 找不到", this.owner.getId(), this.targetId);
            }
        }
    }

    public void sendMessage(int msgType, Object msgData) {
        if (target() != null)
            owner.canvas().sendMessage(owner, msgType, msgData, targetId);
    }

}
