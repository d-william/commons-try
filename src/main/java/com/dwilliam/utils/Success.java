package com.dwilliam.utils;

import java.util.Optional;
import java.util.function.Supplier;

public class Success<T> implements Try<T> {

    final private T t;

    public Success(T t) {
        this.t = t;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return this.t;
    }

    @Override
    public T getOrElse(T elseValue) {
        return this.t;
    }

    @Override
    public Optional<T> asOptional() {
        return Optional.ofNullable(this.t);
    }

    @Override
    public Try<T> orElse(Supplier<T> supplier) {
        return new Failure<>(Try.TRY_NOT_FAILURE);
    }

    @Override
    public Try<Void> orElse(TryProcedure procedure) {
        return new Failure<>(Try.TRY_NOT_FAILURE);
    }

    @Override
    public <T> Try<T> andThen(Supplier<T> supplier) {
        return Try.of(supplier);
    }

    @Override
    public Try<Void> andThen(TryProcedure procedure) {
        return Try.of(procedure);
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(Try.TRY_NOT_FAILURE);
    }

}
