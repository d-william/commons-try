package com.dwilliam.utils;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryConsumer<T> extends Consumer<T>, TryFunction<T, Void> {

    @Override
    default Void apply(T t) {
        accept(t);
        return null;
    }

}
