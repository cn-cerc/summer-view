package cn.cerc.ui.ssr.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Binders implements Iterable<Binder<?>> {

    private List<Binder<?>> items = new ArrayList<>();

    public void add(Binder<?> binder) {
        items.add(binder);
    }

    public void remove(Binder<?> binder) {
        items.remove(binder);
    }

    @Override
    public Iterator<Binder<?>> iterator() {
        return items.iterator();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> findOwner(Class<T> clazz) {
        T item = null;
        for (Binder<?> binder : items) {
            if (binder.owner() != null && clazz.isInstance(binder.owner()))
                item = (T) binder.owner();
        }
        return Optional.ofNullable(item);
    }

    /** 通知发送 */
    public void sendMessage(Object sender, int msgType, Object msgData, String targetId) {
        for (var binder : this.items)
            binder.owner().onMessage(sender, msgType, msgData, targetId);
    }
}
