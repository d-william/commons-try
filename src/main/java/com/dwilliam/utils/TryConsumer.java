package com.dwilliam.utils;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface TryConsumer<T> extends Consumer<T>, Function<T, Void> {

    @Override
    default Void apply(T t) {
        accept(t);
        return null;
    }

    static <T> TryConsumer<T> from(Consumer<T> consumer) {
        return consumer::accept;
    }

}
