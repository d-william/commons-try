package com.dwilliam.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    public Throwable getThrowable() {
        throw new UnsupportedOperationException();
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
    public Optional<Throwable> throwableAsOptional() {
        return Optional.empty();
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Try.newTry(() -> mapper.apply(this.value));
    }

    @Override
    public <U> Try<U> map(TryFunction<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Try.newTry(() -> mapper.apply(this.value));
    }

    @Override
    public Try<T> mapThrowable(Function<? super Throwable, ? extends Throwable> mapper) {
        Objects.requireNonNull(mapper);
        return this;
    }

    @Override
    public Try<T> mapThrowable(TryFunction<? super Throwable, ? extends Throwable> mapper) {
        Objects.requireNonNull(mapper);
        return this;
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return mapper.apply(this.value);
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public <U> Try<U> flatMap(TryFunction<? super T, ? extends Try<U>> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return mapper.apply(this.value);
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    @Override
    public void consume(Consumer<T> consumer) {
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
    public <U> Try<U> thenValue(U value) {
        return new Success<>(value);
    }

    @Override
    public <U> Try<U> then(Supplier<U> supplier) {
        return Try.of(supplier);
    }

    @Override
    public <U> Try<U> then(TrySupplier<U> supplier) {
        return Try.newTry(supplier);
    }

    @Override
    public Try<T> thenThrowable(Throwable throwable) {
        return this;
    }

    @Override
    public Try<T> thenThrowable(Supplier<Throwable> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<T> thenThrowable(TrySupplier<Throwable> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<T> recoverValue(T value) {
        return this;
    }

    @Override
    public Try<T> recover(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<T> recover(TrySupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<T> recover(Function<? super Throwable, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        return this;
    }

    @Override
    public Try<T> recover(TryFunction<? super Throwable, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        return this;
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException());
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
