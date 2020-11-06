package com.dwilliam.utils;

@FunctionalInterface
public interface TrySupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Throwable;

}
