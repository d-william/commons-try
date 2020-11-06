package com.dwilliam.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Try<T> {

    /**
     * Returns {@code true} if this {@code Try} is a {@code Failure}.
     *
     * @return {@code true} if this {@code Try} is a {@code Failure}, {@code false} otherwise
     */
    boolean isFailure();

    /**
     * Returns {@code true} if this {@code Try} is a {@code Success}.
     *
     * @return {@code true} if this {@code Try} is a {@code Success}, {@code false} otherwise
     */
    boolean isSuccess();

    /**
     * Returns the value in case of {@code Success}
     * or throws the caught {@code Throwable} in case of {@code Failure}.
     *
     * @return the value if this {@code Try} is a {@code Success}
     * @throws Throwable if this {@code Try} is a {@code Failure}
     */
    T get() throws Throwable;

    /**
     * Throws the caught {@code Throwable} if this {@code Try} is a {@code Failure}.
     * Throws a {@code UnsupportedOperationException} if this {@code Try} is a {@code Success}.
     *
     * @throws UnsupportedOperationException if this {@code Try} is a {@code Success}
     * @throws Throwable if this {@code Try} is a {@code Failure}
     */
    void throwThrowable() throws Throwable;

    /**
     * Returns the value if this {@code Try} is a {@code Success}
     * or the given elseValue argument if this {@code Try} is a {@code Failure}.
     *
     * @param elseValue the value that will be return if this {@code Try} is a {@code Failure}
     * @return the value if this {@code Try} is a {@code Success}
     *         or the given elseValue argument if this {@code Try} is a {@code Failure}
     */
    T getOrElse(T elseValue);

    /**
     * Returns an {@code Optional} containing the value if this {@code Try} is a {@code Success}
     * or an empty {@code Optional} if this {@code Try} is a {@code Failure}.
     *
     * @return an {@code Optional} containing the value if this {@code Try} is a {@code Success}
     *         or an empty {@code Optional} if this {@code Try} is a {@code Failure}
     */
    Optional<T> asOptional();

    /**
     * Returns this {@code Try} if it's a {@code Success}
     * or the given elseTry argument if it's a @code Failure}.
     *
     * @param elseTry the {@code Try} that will be return if this {@code Try} is a {@code Failure}
     * @return this {@code Try} if it's a {@code Success}
     *         or the given elseTry argument if it's a @code Failure}
     */
    Try<T> orElse(Try<T> elseTry);

    /**
     * If this {@code Try} is a {@code Success}, returns an {@code Optional} describing
     * the result of applying the given mapping function to the value,
     * otherwise returns an empty {@code Optional}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code Optional} describing the result of applying the given mapping
     *         function to the value, otherwise returns an empty {@code Optional}.
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Optional<U> map(Function<? super T, ? extends U> mapper);

    /**
     * If this {@code Try} is a {@code Failure}, returns an {@code Optional} describing
     * the result of applying the given mapping function to the caught {@code Throwable},
     * otherwise returns an empty {@code Optional}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code Optional} describing the result of applying the given mapping
     *         function to the caught {@code Throwable}, otherwise returns an empty {@code Optional}.
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Optional<U> mapThrowable(Function<? super Throwable, ? extends U> mapper);

    /**
     * Performs the given action on the value if this {@code Try} is a {@code Success}.
     * Does nothing if this {@code Try} is a {@code Failure}.
     *
     * @param consumer The action
     * @throws NullPointerException if the specified action is {@code null}
     */
    void forEach(Consumer<T> consumer);

    /**
     * Performs the given action on the caught {@code Throwable} if this {@code Try} is a {@code Failure}.
     * Does nothing if this {@code Try} is a {@code Success}.
     * It's like {@code void forEach(Consumer<T>);} for the {@code Throwable}.
     *
     * @param consumer The action
     * @throws NullPointerException if the specified action is {@code null}
     */
    void trap(Consumer<Throwable> consumer);


    /**
     * Returns {@code true} if this {@code Try} is a {@code Success} and the value match
     * the given predicate, {@code false} otherwise
     *
     * @param predicate a predicate which test the value.
     * @return {@code true} if this {@code Try} is a {@code Success} and the value match the given predicate,
     *         {@code false} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    boolean contains(Predicate<T> predicate);

    /**
     * Returns {@code true} if this {@code Try} is a {@code Failure} and the the caught {@code Throwable} match
     * the given predicate, {@code false} otherwise
     *
     * @param predicate a predicate which test the the caught {@code Throwable}.
     * @return {@code true} if this {@code Try} is a {@code Failure} and the the caught {@code Throwable}
     *         match the given predicate, {@code false} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    boolean containsThrowable(Predicate<Throwable> predicate);

    /**
     * Converts this to a {@code Failure} if the predicate is not satisfied.
     *
     * @param predicate a predicate which test the value.
     * @return this if it's a {@code Failure} or if it's a {@code Success} and the value match the given predicate,
     *         a {@code Failure} containing a {@code NoSuchElementException} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    Try<T> filter(Predicate<T> predicate);

    /**
     * Converts this to a {@code Failure} if the predicate is not satisfied.
     *
     * @param predicate a predicate which test the value.
     * @return this if it's a {@code Success} or if it's a {@code Failure} and the the caught {@code Throwable}
     *         match the given predicate, a {@code Failure} containing a {@code NoSuchElementException} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    Try<T> filterThrowable(Predicate<Throwable> predicate);

    <U> Try<U> then(TrySupplier<? extends U> supplier);

    default Try<Void> then(TryProcedure procedure) {
        return then((TrySupplier<Void>) procedure);
    }

    <U> Try<U> then(Function<? super T, ? extends U> function);

    default Try<Void> then(TryConsumer<? super T> consumer) {
        return then((Function<? super T, Void>) consumer);
    }

    default Try<Void> then(Consumer<? super T> consumer) {
        return then(TryConsumer.from(consumer));
    }

    <U> Try<U> then(Function<? super T, ? extends U> success, Function<? super Throwable, ? extends U> failure);

    default Try<Void> then(TryConsumer<? super T> success, TryConsumer<? super Throwable> failure) {
        return then(success, (Function<? super Throwable, Void>) failure);
    }

    default Try<Void> then(TryConsumer<? super T> success, Consumer<? super Throwable> failure) {
        return then(success, TryConsumer.from(failure));
    }

    default Try<Void> then(Consumer<? super T> success, TryConsumer<? super Throwable> failure) {
        return then(TryConsumer.from(success), failure);
    }

    default Try<Void> then(Consumer<? super T> success, Consumer<? super Throwable> failure) {
        return then(TryConsumer.from(success), TryConsumer.from(failure));
    }

    Try<T> recover(TrySupplier<? extends T> supplier);

    Try<T> recover(Function<? super Throwable, ? extends T> function);

    /**
     * Inverts this {@code Try}.
     * Returns its exception wrapped in a {@code Success} if it's a {@code Failure}
     * or a {@code Failure} containing an {@code UnsupportedOperationException} if it's a {@code Success}.
     *
     * @return a {@code Try<Throwable>}
     */
    Try<Throwable> failed();

    static <T> Try<T> of(T object) {
        return new Success<>(object);
    }

    static <T> Try<T> of(TrySupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Success<>(supplier.get());
        }
        catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    Throwable TRY_NOT_FAILURE = new UnsupportedOperationException("Try is not in failure");

}
