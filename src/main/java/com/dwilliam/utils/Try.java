package com.dwilliam.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Try<T> permits Failure, Success {

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
     * Returns the value from this {@code Success} or
     * throws the caught {@code Throwable} in case of {@code Failure}.
     *
     * @return the value if this {@code Try} is a {@code Success}
     * @throws NoSuchElementException if this {@code Try} is a {@code Failure}
     */
    T get();

    /**
     * Alias for {@link #get()}
     *
     * @return the value if this {@code Try} is a {@code Success}
     * @throws NoSuchElementException if this {@code Try} is a {@code Failure}
     */
    T value();

    /**
     * Returns the throwable from this {@code Failure} or
     * throws a {@code UnsupportedOperationException} if this {@code Try} is a {@code Success}.
     *
     * @return the throwable if this {@code Try} is a {@code Failure}
     * @throws UnsupportedOperationException if this {@code Try} is a {@code Success}
     */
    Throwable getThrowable();

    /**
     * Throws the caught {@code Throwable} if this {@code Try} is a {@code Failure} or
     * throws a {@code UnsupportedOperationException} if this {@code Try} is a {@code Success}.
     *
     * @throws UnsupportedOperationException if this {@code Try} is a {@code Success}
     * @throws Throwable if this {@code Try} is a {@code Failure}
     */
    default void throwThrowable() throws Throwable {
        throw getThrowable();
    }

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
     * Returns an {@code Optional} containing the throwable if this {@code Try} is a {@code Failure}
     * or an empty {@code Optional} if this {@code Try} is a {@code Success}.
     *
     * @return an {@code Optional} containing the value if this {@code Try} is a {@code Failure}
     *         or an empty {@code Optional} if this {@code Try} is a {@code Success}
     */
    Optional<Throwable> throwableAsOptional();

    /**
     * Returns this {@code Try} if it's a {@code Success}
     * or the given elseTry argument if it's a @code Failure}.
     *
     * @param elseTry the {@code Try} that will be return if this {@code Try} is a {@code Failure}
     * @return this {@code Try} if it's a {@code Success}
     *         or the given elseTry argument if it's a @code Failure}
     */
    default Try<T> orElse(Try<T> elseTry) {
        return isSuccess() ? this : elseTry;
    }

    /**
     * Maps the given function to the value if it's a {@code Success}
     * or returns this if it's a {@code Failure}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a new {@code Try} if it's a {@code Success} or this if it's a {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Try<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Likes {@code map(Function<? super T, ? extends U>)} but with a {@code TryFunction}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a new {@code Try} if it's a {@code Success} or this if it's a {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Try<U> map(TryFunction<? super T, ? extends U> mapper);

    /**
     * Maps the given function to the throwable if it's a {@code Failure}
     * or returns this if it's a {@code Success}.
     *
     * @param mapper the mapping function to apply to a throwable
     * @return a new {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    Try<T> mapThrowable(Function<? super Throwable, ? extends Throwable> mapper);

    /**
     * Likes {@code mapThrowable(Function<? super T, ? extends Throwable>)} but with a {@code TryFunction}.
     *
     * @param mapper the mapping function to apply to a throwable
     * @return a new {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    Try<T> mapThrowable(TryFunction<? super Throwable, ? extends Throwable> mapper);

    /**
     * Maps the given function to the value if it's a {@code Success}
     * or returns this if it's a {@code Failure}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a new {@code Try} if it's a {@code Success} or this if it's a {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper);

    /**
     * Likes {@code flatMap(Function<? super T, ? extends Try<U>>)} but with a {@code TryFunction}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a new {@code Try} if it's a {@code Success} or this if it's a {@code Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Try<U> flatMap(TryFunction<? super T, ? extends Try<U>> mapper);

    /**
     * Performs the given action on the value if this {@code Try} is a {@code Success}.
     * Does nothing if this {@code Try} is a {@code Failure}.
     *
     * @param consumer The action
     * @throws NullPointerException if the specified action is {@code null}
     */
    void consume(Consumer<T> consumer);

    /**
     * Performs the given action on the caught {@code Throwable} if this {@code Try} is a {@code Failure}.
     * Does nothing if this {@code Try} is a {@code Success}.
     * It's like {@code void consume(Consumer<T>);} for the {@code Throwable}.
     *
     * @param consumer The action
     * @throws NullPointerException if the specified action is {@code null}
     */
    void trap(Consumer<Throwable> consumer);


    /**
     * Returns {@code true} if this {@code Try} is a {@code Success} and the value match
     * the given predicate, {@code false} otherwise.
     *
     * @param predicate a predicate which test the value.
     * @return {@code true} if this {@code Try} is a {@code Success} and the value match the given predicate,
     *         {@code false} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    boolean contains(Predicate<T> predicate);

    /**
     * Returns {@code true} if this {@code Try} is a {@code Failure} and the the caught {@code Throwable} match
     * the given predicate, {@code false} otherwise.
     *
     * @param predicate a predicate which test the the caught {@code Throwable}.
     * @return {@code true} if this {@code Try} is a {@code Failure} and the the caught {@code Throwable}
     *         match the given predicate, {@code false} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    boolean containsThrowable(Predicate<Throwable> predicate);

    /**
     * Converts this to a {@code Failure} if the predicate on the value is not satisfied.
     *
     * @param predicate a predicate which test the value.
     * @return this if it's a {@code Failure} or if it's a {@code Success} and the value match the given predicate,
     *         a {@code Failure} containing a {@code NoSuchElementException} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    Try<T> filter(Predicate<T> predicate);

    /**
     * Converts this to a {@code Failure} if the predicate on the throwable is not satisfied.
     *
     * @param predicate a predicate which test the throwable.
     * @return this if it's a {@code Failure} and the the caught {@code Throwable} match the given predicate,
     *         a {@code Failure} containing a {@code UnsupportedOperationException} if it's a {@code Success},
     *         a {@code Failure} containing a {@code NoSuchElementException} otherwise
     * @throws NullPointerException if the specified predicate is {@code null}
     */
    Try<T> filterThrowable(Predicate<Throwable> predicate);

    /**
     * Returns a new {@code Success} build with the specified value if this is a {@code Success}.
     * Returns this if it's a {@code Failure}.
     *
     * @param value a supplier used to make the new {@code Try}
     * @param <U> The type of the value
     * @return a new {@code Try} if this is a {@code Success}, this if it's a {@code Failure}
     */
    <U> Try<U> thenValue(U value);

    /**
     * Returns a new {@code Try} build with the specified supplier if this is a {@code Success}.
     * Returns this if this is a {@code Failure}.
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @param <U> The type of the value returned by the supplier
     * @return a new {@code Try} if this is a {@code Success}, this if it's a {@code Failure}
     */
    <U> Try<U> then(Supplier<U> supplier);

    /**
     * Likes {@code then(Supplier<U>)} but with a {@code TrySupplier}
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @param <U> The type of the value returned by the supplier
     * @return a new {@code Try} if this is a {@code Success}, this if it's a {@code Failure}
     */
    <U> Try<U> then(TrySupplier<U> supplier);

    /**
     * Returns a new {@code Failure} build with the specified throwable if this is a {@code Failure}.
     * Returns this if this is a {@code Success}.
     *
     * @param throwable a supplier used to make the new {@code Try}
     * @return a new {@code Try} if this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> thenThrowable(Throwable throwable);

    /**
     * Returns a new {@code Try} build with the specified supplier if this is a {@code Failure}.
     * Returns this if this is a {@code Success}.
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @return a new {@code Try} if this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> thenThrowable(Supplier<Throwable> supplier);

    /**
     * Likes {@code thenThrowable(Supplier<Throwable>)} but with a {@code TrySupplier}
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @return a new {@code Try} if this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> thenThrowable(TrySupplier<Throwable> supplier);

    /**
     * Returns a new {@code Success} build with the specified value if this is a {@code Failure}.
     * Returns this if this is a {@code Success}.
     *
     * @param value a supplier used to make the new {@code Try}
     * @return a new {@code Try} is this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> recoverValue(T value);

    /**
     * Returns a new {@code Try} build with the specified supplier if this is a {@code Success}.
     * Returns this if this is a {@code Failure}.
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @return a new {@code Try} is this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> recover(Supplier<? extends T> supplier);

    /**
     * Likes {@code recover(Supplier<U>)} but with a {@code TrySupplier}
     *
     * @param supplier a supplier used to make the new {@code Try}
     * @return a new {@code Try} is this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> recover(TrySupplier<? extends T> supplier);

    /**
     * Returns a new {@code Try} build with the specified mapper if this is a {@code Failure}.
     * Returns this if this is a {@code Success}.
     *
     * @param mapper a mapper used to make the new {@code Try}
     * @return a new {@code Try} is this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> recover(Function<? super Throwable, ? extends T> mapper);

    /**
     * Likes {@code recover(Function<? super Throwable, ? extends T>)} but with a {@code TryFunction}
     *
     * @param mapper a mapper used to make the new {@code Try}
     * @return a new {@code Try} is this is a {@code Failure}, this if it's a {@code Success}
     */
    Try<T> recover(TryFunction<? super Throwable, ? extends T> mapper);

    /**
     * Inverts this {@code Try}.
     * Returns its exception wrapped in a {@code Success} if it's a {@code Failure}
     * or a {@code Failure} containing an {@code UnsupportedOperationException} if it's a {@code Success}.
     *
     * @return a {@code Try<Throwable>}
     */
    Try<Throwable> failed();

    /**
     * Tries a value.
     *
     * @param value the value contained in the {@code Try}
     * @param <T> the type of the value contained in the {@code Try}
     * @return a {@code Try}
     */
    static <T> Try<T> value(T value) {
        return new Success<>(value);
    }

    /**
     * Tries some code.
     *
     * @param supplier the supplier used to make the {@code Try}
     * @param <T> the type of the value contained in the {@code Try}
     * @return a {@code Try}
     * @throws NullPointerException if the supplier consumer is {@code null}
     */
    static <T> Try<T> of(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Success<>(supplier.get());
        }
        catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    /**
     * Tries some code.
     *
     * @param supplier the supplier used to make the {@code Try}
     * @param <T> the type of the value contained in the {@code Try}
     * @return a {@code Try}
     * @throws NullPointerException if the supplier consumer is {@code null}
     */
    static <T> Try<T> newTry(TrySupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Success<>(supplier.get());
        }
        catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

}
