package com.dwilliam.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record Failure<T>(Throwable throwable) implements Try<T> {

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get() {
        throw new NoSuchElementException(this.throwable);
    }

    @Override
    public T value() {
        throw new NoSuchElementException(this.throwable);
    }

    @Override
    public Throwable getThrowable() {
        return this.throwable;
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
    public Optional<Throwable> throwableAsOptional() {
        return Optional.ofNullable(this.throwable);
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Failure<>(this.throwable);
    }

    @Override
    public <U> Try<U> map(TryFunction<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Failure<>(this.throwable);
    }

    @Override
    public Try<T> mapThrowable(Function<? super Throwable, ? extends Throwable> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return new Failure<>(mapper.apply(this.throwable));
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public Try<T> mapThrowable(TryFunction<? super Throwable, ? extends Throwable> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return new Failure<>(mapper.apply(this.throwable));
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        Objects.requireNonNull(mapper);
        return new Failure<>(this.throwable);
    }

    @Override
    public <U> Try<U> flatMap(TryFunction<? super T, ? extends Try<U>> mapper) {
        Objects.requireNonNull(mapper);
        return new Failure<>(this.throwable);
    }

    @Override
    public void consume(Consumer<T> consumer) {
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
    public <U> Try<U> thenValue(U value) {
        return new Failure<>(this.throwable);
    }

    @Override
    public <U> Try<U> then(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);
        return new Failure<>(this.throwable);
    }

    @Override
    public <U> Try<U> then(TrySupplier<U> supplier) {
        Objects.requireNonNull(supplier);
        return new Failure<>(this.throwable);
    }

    @Override
    public Try<T> thenThrowable(Throwable throwable) {
        return new Failure<>(throwable);
    }

    @Override
    public Try<T> thenThrowable(Supplier<Throwable> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Failure<>(supplier.get());
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public Try<T> thenThrowable(TrySupplier<Throwable> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Failure<>(supplier.get());
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public Try<T> recoverValue(T value) {
        return new Success<>(value);
    }

    @Override
    public Try<T> recover(Supplier<? extends T> supplier) {
        return Try.of(supplier);
    }

    @Override
    public Try<T> recover(TrySupplier<? extends T> supplier) {
        return Try.newTry(supplier);
    }

    @Override
    public Try<T> recover(Function<? super Throwable, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return new Success<>(mapper.apply(this.throwable));
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public Try<T> recover(TryFunction<? super Throwable, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return new Success<>(mapper.apply(this.throwable));
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
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
