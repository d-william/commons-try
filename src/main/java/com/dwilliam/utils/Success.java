package com.dwilliam.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Success<T> implements Try<T> {

    final private T value;

    public Success(T t) {
        this.value = t;
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
        return this.value;
    }

    @Override
    public void throwThrowable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T getOrElse(T elseValue) {
        return this.value;
    }

    @Override
    public Optional<T> asOptional() {
        return Optional.ofNullable(this.value);
    }

    @Override
    public Try<T> orElse(Try<T> elseTry) {
        return this;
    }

    @Override
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return asOptional().map(mapper);
    }

    @Override
    public <U> Optional<U> mapThrowable(Function<? super Throwable, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Optional.empty();
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer).accept(this.value);
    }

    @Override
    public void trap(Consumer<Throwable> consumer) {
        Objects.requireNonNull(consumer);
    }

    @Override
    public boolean contains(Predicate<T> predicate) {
        return Objects.requireNonNull(predicate).test(this.value);
    }

    @Override
    public boolean containsThrowable(Predicate<Throwable> predicate) {
        Objects.requireNonNull(predicate);
        return false;
    }

    @Override
    public Try<T> filter(Predicate<T> predicate) {
        return Objects.requireNonNull(predicate).test(this.value) ? this : new Failure<>(new NoSuchElementException());
    }

    @Override
    public Try<T> filterThrowable(Predicate<Throwable> predicate) {
        Objects.requireNonNull(predicate);
        return this;
    }

    @Override
    public <U> Try<U> then(TrySupplier<? extends U> supplier) {
        return Try.of(supplier);
    }

    public <U> Try<U> then(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        try {
            return new Success<>(function.apply(this.value));
        }
        catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    public <U> Try<U> then(Function<? super T, ? extends U> success, Function<? super Throwable, ? extends U> failure) {
        Objects.requireNonNull(success);
        Objects.requireNonNull(failure);
        try {
            return new Success<>(success.apply(this.value));
        }
        catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    @Override
    public Try<T> recover(TrySupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<T> recover(Function<? super Throwable, ? extends T> function) {
        Objects.requireNonNull(function);
        return this;
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(Try.TRY_NOT_FAILURE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Success<?> success = (Success<?>) o;
        return Objects.equals(value, success.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
