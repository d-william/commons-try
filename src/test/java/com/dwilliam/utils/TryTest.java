package com.dwilliam.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TryTest {

    static final Exception exception = new IllegalAccessException();
    
    Try<Integer> success;
    Try<Integer> failure;
    Try<Integer> nullTry;

    @BeforeEach
    void beforeEach() {
        success = Try.value(0);
        failure = Try.newTry(() -> { throw exception; });
        nullTry = Try.value(null);
    }

    @Test
    void isFailure() {
        assertFalse(success.isFailure());
        assertTrue(failure.isFailure());

        assertFalse(nullTry.isFailure());
    }

    @Test
    void isSuccess() {
        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());

        assertTrue(nullTry.isSuccess());
    }

    @Test
    void get() throws Throwable {
        assertEquals(0, success.get());
        assertThrows(Exception.class, () -> failure.get());

        assertNull(nullTry.get());
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    void getThrowable() {
        assertThrows(UnsupportedOperationException.class, () -> success.getThrowable());
        assertEquals(exception, failure.getThrowable());

        assertThrows(UnsupportedOperationException.class, () -> nullTry.getThrowable());
    }

    @Test
    void throwThrowable() {
        assertThrows(UnsupportedOperationException.class, () -> success.throwThrowable());
        assertThrows(exception.getClass(), () -> failure.throwThrowable());

        assertThrows(UnsupportedOperationException.class, () -> nullTry.throwThrowable());
    }

    @Test
    void getOrElse() {
        assertEquals(0, success.getOrElse(1));
        assertEquals(0, failure.getOrElse(0));

        assertNull(nullTry.getOrElse(0));
    }

    @Test
    void asOptional() {
        assertEquals(Optional.of(0), success.asOptional());
        assertEquals(Optional.empty(), failure.asOptional());

        assertEquals(Optional.empty(), nullTry.asOptional());
    }

    @Test
    void throwableAsOptional() {
        assertEquals(Optional.empty(), success.throwableAsOptional());
        assertEquals(Optional.of(exception), failure.throwableAsOptional());

        assertEquals(Optional.empty(), nullTry.throwableAsOptional());
    }

    @Test
    void orElse() {
        assertEquals(success, success.orElse(Try.of(() -> 1)));
        assertEquals(success, failure.orElse(success));

        assertEquals(nullTry, nullTry.orElse(Try.of(() -> 1)));
    }

    @Test
    void map() {
        assertEquals(Try.value(1), success.map((Function<Integer, Integer>) x -> 1));
        assertEquals(Try.value(1), success.map((TryFunction<Integer, Integer>) x -> 1));
        assertEquals(failure, failure.map((Function<Integer, Integer>) x -> 1));
        assertEquals(failure, failure.map((TryFunction<Integer, Integer>) x -> 1));

        assertEquals(Try.value(1), nullTry.map((Function<Integer, Integer>) x -> 1));
        assertEquals(Try.value(1), nullTry.map((TryFunction<Integer, Integer>) x -> 1));
    }

    @Test
    void mapThrowable() {
        assertEquals(success, success.mapThrowable((Function<Throwable, Throwable>) t -> t));
        assertEquals(success, success.mapThrowable((TryFunction<Throwable, Throwable>) t -> t));
        Exception exception = new Exception();
        assertEquals(exception, failure.mapThrowable((Function<Throwable, Throwable>) t -> exception).getThrowable());
        assertEquals(exception, failure.mapThrowable((TryFunction<Throwable, Throwable>) t -> exception).getThrowable());

        assertEquals(nullTry, nullTry.mapThrowable((Function<Throwable, Throwable>) t -> t));
        assertEquals(nullTry, nullTry.mapThrowable((TryFunction<Throwable, Throwable>) t -> t));
    }

    @SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
    @Test
    void flatMap() {
        assertEquals(Try.value(""), Try.value(success).flatMap((Function<Try<Integer>, Try<String>>) x -> Try.value("")));
        assertEquals(Try.value(""), Try.value(success).flatMap((TryFunction<Try<Integer>, Try<String>>) x -> Try.value("")));

        Try<Try<Integer>> failure = Try.newTry(() -> { throw exception; });
        assertEquals(failure, failure.flatMap((Function<Try<Integer>, Try<String>>) x -> Try.value("")));
        assertEquals(failure, failure.flatMap((TryFunction<Try<Integer>, Try<String>>) x -> Try.value("")));
    }

    @Test
    void consume() {
        assertThrows(RuntimeException.class, () -> success.consume(i -> { throw new RuntimeException(); }));
        assertDoesNotThrow(() -> failure.consume(i -> { throw new RuntimeException(); }));

        assertThrows(RuntimeException.class, () -> nullTry.consume(i -> { throw new RuntimeException(); }));
    }

    @Test
    void trap() {
        assertDoesNotThrow(() -> success.trap(i -> { throw new RuntimeException(); }));
        assertThrows(RuntimeException.class, () -> failure.trap(e -> { throw new RuntimeException(); }));

        assertDoesNotThrow(() -> nullTry.trap(e -> { throw new RuntimeException(); }));
    }

    @Test
    void contains() {
        assertTrue(success.contains(x -> x==0));
        assertFalse(success.contains(x -> x==1));
        assertFalse(failure.contains(Objects::nonNull));

        assertTrue(nullTry.contains(Objects::isNull));
        assertFalse(nullTry.contains(Objects::nonNull));
    }

    @Test
    void containsThrowable() {
        assertFalse(success.containsThrowable(Objects::nonNull));
        assertTrue(failure.containsThrowable(Objects::nonNull));
        assertFalse(failure.containsThrowable(Objects::isNull));

        assertFalse(nullTry.containsThrowable(Objects::isNull));
    }

    @Test
    void filter() {
        assertEquals(success, success.filter(x -> x == 0));
        assertThrows(NoSuchElementException.class, () -> success.filter(x -> x == 1).get());
        assertThrows(exception.getClass(), () -> failure.filter(x -> x == 0).get());

        assertEquals(nullTry, nullTry.filter(Objects::isNull));
        assertThrows(NoSuchElementException.class, () -> nullTry.filter(Objects::nonNull).get());
    }

    @Test
    void filterThrowable() {
        assertDoesNotThrow(() -> success.filterThrowable(o -> o.equals(exception)).get());
        assertThrows(exception.getClass(), () -> failure.filterThrowable(o -> o.equals(exception)).get());
        assertThrows(NoSuchElementException.class, () -> failure.filterThrowable(o -> !o.equals(exception)).get());

        assertEquals(nullTry, nullTry.filter(Objects::isNull));
        assertThrows(NoSuchElementException.class, () -> nullTry.filter(Objects::nonNull).get());
    }

    @Test
    void then() {
        assertEquals(Try.value(1), success.thenValue(1));
        assertEquals(Try.value(1), success.then((Supplier<Integer>) () -> 1));
        assertEquals(Try.value(1), success.then((TrySupplier<Integer>) () -> 1));

        assertEquals(failure, failure.thenValue(1));
        assertEquals(failure, failure.then((Supplier<Integer>) () -> 1));
        assertEquals(failure, failure.then((TrySupplier<Integer>) () -> 1));
    }

    @Test
    void thenThrowable() {
        Exception exception = new Exception();

        assertEquals(success, success.thenThrowable(exception));
        assertEquals(success, success.thenThrowable((Supplier<Throwable>) () -> exception));
        assertEquals(success, success.thenThrowable((TrySupplier<Throwable>) () -> exception));
        assertEquals(Try.newTry(() -> { throw exception; }), failure.thenThrowable(exception));
        assertEquals(Try.newTry(() -> { throw exception; }), failure.thenThrowable((TrySupplier<Throwable>) () -> exception));
        assertEquals(Try.newTry(() -> { throw exception; }), failure.thenThrowable((TrySupplier<Throwable>) () -> exception));
    }

    @Test
    void recover() {
        assertEquals(success, success.recoverValue(1));
        assertEquals(success, success.recover((Supplier<Integer>) () -> 1));
        assertEquals(success, success.recover((TrySupplier<Integer>) () -> 1));
        assertEquals(success, success.recover((Function<Throwable, Integer>) t -> 1));
        assertEquals(success, success.recover((TryFunction<Throwable, Integer>) t -> 1));

        assertEquals(Try.value(1), failure.recoverValue(1));
        assertEquals(Try.value(1), failure.recover((Supplier<Integer>) () -> 1));
        assertEquals(Try.value(1), failure.recover((TrySupplier<Integer>) () -> 1));
        assertEquals(Try.value(1), failure.recover((Function<Throwable, Integer>) t -> 1));
        assertEquals(Try.value(1), failure.recover((TryFunction<Throwable, Integer>) t -> 1));
    }

    @Test
    void failed() {
        assertThrows(UnsupportedOperationException.class, () -> success.failed().get());
        assertDoesNotThrow(() -> failure.failed().get());

        assertThrows(UnsupportedOperationException.class, () -> nullTry.failed().get());
    }

}