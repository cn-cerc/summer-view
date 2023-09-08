package cn.cerc.ui.ssr.service;

import java.util.function.Consumer;
import java.util.function.Supplier;

import cn.cerc.mis.ado.CustomEntity;

public abstract class AbstractEntityOpenHelper<T extends CustomEntity> {

    public abstract <X extends Throwable> AbstractEntityOpenHelper<T> isEmptyThrow(
            Supplier<? extends X> exceptionSupplier) throws X;

    public abstract <X extends Throwable> AbstractEntityOpenHelper<T> isPresentThrow(
            Supplier<? extends X> exceptionSupplier) throws X;

    public abstract AbstractEntityOpenHelper<T> update(Consumer<T> action);

    public abstract T insert(Consumer<T> action);

    public abstract void delete();

}
