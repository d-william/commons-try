package com.dwilliam.utils;

import java.util.Optional;
import java.util.function.Supplier;

public class Failure<T> implements Try<T> {

    final private Throwable throwable;

    Failure(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get() throws Throwable {
        throw this.throwable;
    }

    @Override
    public T getOrElse(T elseValue) {
        return elseValue;
    }

    @Override
    public Optional<T> asOptional() {
        return Optional.empty();
    }

    @Override
    public Try<T> orElse(Supplier<T> supplier) {
        return Try.of(supplier);
    }

    @Override
    public Try<Void> orElse(TryProcedure procedure) {
        return Try.of(procedure);
    }

    @Override
    public <T> Try<T> andThen(Supplier<T> supplier) {
        return new Failure<>(this.throwable);
    }

    @Override
    public Try<Void> andThen(TryProcedure procedure) {
        return new Failure<>(this.throwable);
    }

    @Override
    public Try<Throwable> failed() {
        return new Success<>(this.throwable);
    }

}
