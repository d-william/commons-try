package com.dwilliam.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface Try<A> {

    boolean isFailure();

    boolean isSuccess();

    A get() throws Throwable;

    A getOrElse(A elseValue);

    Optional<A> asOptional();

    Try<A> orElse(Supplier<A> supplier);

    Try<Void> orElse(TryProcedure procedure);

    <B> Try<B> andThen(Supplier<B> supplier);

    Try<Void> andThen(TryProcedure procedure);

    Try<Throwable> failed();

    static <T> Try<T> of(Supplier<T> supplier) {
        try {
            Objects.requireNonNull(supplier);
            return new Success<>(supplier.get());
        }
        catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    static Try<Void> of(TryProcedure procedure) {
        try {
            Objects.requireNonNull(procedure);
            procedure.run();
            return new Success<>(null);
        }
        catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    Throwable TRY_NOT_FAILURE = new UnsupportedOperationException("Try is not in failure");

}
