package com.dwilliam.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public void throwThrowable() throws Throwable {
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
    public Try<T> orElse(Try<T> elseTry) {
        return elseTry;
    }

    @Override
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Optional.empty();
    }

    @Override
    public <U> Optional<U> mapThrowable(Function<? super Throwable, ? extends U> mapper) {
        return Optional.ofNullable(Objects.requireNonNull(mapper).apply(this.throwable));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
    }

    @Override
    public void trap(Consumer<Throwable> consumer) {
        Objects.requireNonNull(consumer).accept(this.throwable);
    }

    @Override
    public boolean contains(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return false;
    }

    @Override
    public boolean containsThrowable(Predicate<Throwable> predicate) {
        return Objects.requireNonNull(predicate).test(this.throwable);
    }

    @Override
    public Try<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return this;
    }

    @Override
    public Try<T> filterThrowable(Predicate<Throwable> predicate) {
        return Objects.requireNonNull(predicate).test(this.throwable) ? this : new Failure<>(new NoSuchElementException());
    }

    @Override
    public <U> Try<U> then(TrySupplier<? extends U> supplier) {
        Objects.requireNonNull(supplier);
        return (Try<U>) this;
    }

    @Override
    public <U> Try<U> then(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        return (Try<U>) this;
    }

    public <U> Try<U> then(Function<? super T, ? extends U> success, Function<? super Throwable, ? extends U> failure) {
        Objects.requireNonNull(success);
        Objects.requireNonNull(failure);
        try {
            return new Success<>(failure.apply(this.throwable));
        }
        catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    @Override
    public Try<T> recover(TrySupplier<? extends T> supplier) {
        return Try.of(supplier);
    }

    @Override
    public Try<T> recover(Function<? super Throwable, ? extends T> function) {
        Objects.requireNonNull(function);
        try {
            return new Success<>(function.apply(this.throwable));
        }
        catch (Exception exception) {
            return new Failure<>(exception);
        }
    }

    @Override
    public Try<Throwable> failed() {
        return new Success<>(this.throwable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Failure<?> failure = (Failure<?>) o;
        return Objects.equals(throwable, failure.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(throwable);
    }

}
