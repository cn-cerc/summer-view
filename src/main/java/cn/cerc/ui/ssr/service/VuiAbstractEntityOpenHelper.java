package cn.cerc.ui.ssr.service;

import java.util.function.Consumer;
import java.util.function.Supplier;

import cn.cerc.mis.ado.CustomEntity;

public abstract class VuiAbstractEntityOpenHelper<T extends CustomEntity> {

    public abstract boolean isEmpty();

    public abstract <X extends Throwable> VuiAbstractEntityOpenHelper<T> isEmptyThrow(
            Supplier<? extends X> exceptionSupplier) throws X;

    public abstract boolean isPresent();

    public abstract <X extends Throwable> VuiAbstractEntityOpenHelper<T> isPresentThrow(
            Supplier<? extends X> exceptionSupplier) throws X;

    public abstract VuiAbstractEntityOpenHelper<T> update(Consumer<T> action);

    public abstract T insert(Consumer<T> action);

    public abstract T delete();

    public abstract T get();

}
