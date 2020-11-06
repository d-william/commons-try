package com.dwilliam.utils;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TryTest {

    @Test
    void isFailure() {
        assertFalse(Try.of(() -> 0).isFailure());
        assertTrue(Try.of(() -> { throw new Exception(); }).isFailure());
    }

    @Test
    void isSuccess() {
        assertTrue(Try.of(() -> 0).isSuccess());
        assertFalse(Try.of(() -> { throw new Exception(); }).isSuccess());
    }

    @Test
    void get() throws Throwable {
        assertEquals(0, Try.of(() -> 0).get());
        assertThrows(Exception.class, () -> Try.of(() -> { throw new Exception(); }).get());

        assertNull(Try.of(() -> null).get());

        boolean aBool = true;

        Try.of(() -> {
            if(aBool) return 42;
            else throw new Exception();
        });
    }

    @Test
    void throwThrowable() {
        assertThrows(UnsupportedOperationException.class, () -> Try.of(() -> 0).throwThrowable());
        assertThrows(IllegalArgumentException.class, () -> Try.of(() -> { throw new IllegalArgumentException(); }).throwThrowable());

        assertThrows(UnsupportedOperationException.class, () -> Try.of(() -> null).throwThrowable());
    }

    @Test
    void getOrElse() {
        assertEquals(0, Try.of(() -> 0).getOrElse(1));
        assertEquals(0, Try.of(() -> { throw new Exception(); }).getOrElse(0));

        assertNull(Try.of(() -> null).getOrElse(0));
    }

    @Test
    void asOptional() {
        assertEquals(Optional.of(0), Try.of(() -> 0).asOptional());
        assertEquals(Optional.empty(), Try.of(() -> { throw new Exception(); }).asOptional());

        assertEquals(Optional.empty(), Try.of(() -> null).asOptional());
    }

    @Test
    void orElse() {
        assertEquals(Try.of(() -> 0), Try.of(() -> 0).orElse(Try.of(() -> 1)));
        assertEquals(Try.of(() -> 0), Try.of(() -> { throw new Exception(); }).orElse(Try.of(() -> 0)));

        assertEquals(Try.of(() -> null), Try.of(() -> null).orElse(Try.of(() -> 1)));
    }

    @Test
    void map() {
        assertEquals(Optional.of(1), Try.of(() -> 0).map(x -> x+1));
        assertEquals(Optional.empty(), Try.of(() -> { throw new Exception(); }).map(Object::toString));

        assertEquals(Optional.empty(), Try.of(() -> null).map(Object::toString));
    }

    @Test
    void mapThrowable() {
        assertEquals(Optional.empty(), Try.of(() -> 0).mapThrowable(Objects::isNull));
        assertEquals(Optional.of(0), Try.of(() -> { throw new Exception(); }).mapThrowable(e -> 0));

        assertEquals(Optional.empty(), Try.of(() -> null).mapThrowable(Objects::isNull));
    }

    @Test
    void forEach() {
        assertThrows(RuntimeException.class, () -> Try.of(() -> 0).forEach(i -> { throw new RuntimeException(); }));
        assertDoesNotThrow(() -> Try.of(() -> { throw new Exception(); }).forEach(i -> { throw new RuntimeException(); }));

        assertThrows(RuntimeException.class, () -> Try.of(() -> null).forEach(i -> { throw new RuntimeException(); }));
    }

    @Test
    void trap() {
        assertDoesNotThrow(() -> Try.of(() -> 0).trap(i -> { throw new RuntimeException(); }));
        assertThrows(RuntimeException.class, () -> Try.of(() -> { throw new Exception(); }).trap(e -> { throw new RuntimeException(); }));

        assertDoesNotThrow(() -> Try.of(() -> null).trap(e -> { throw new RuntimeException(); }));
    }

    @Test
    void contains() {
        assertTrue(Try.of(() -> 0).contains(x -> x==0));
        assertFalse(Try.of(() -> 0).contains(x -> x==1));
        assertFalse(Try.of(() -> { throw new Exception(); }).contains(Objects::nonNull));

        assertTrue(Try.of(() -> null).contains(Objects::isNull));
        assertFalse(Try.of(() -> null).contains(Objects::nonNull));
    }

    @Test
    void containsThrowable() {
        assertFalse(Try.of(() -> 0).containsThrowable(Objects::nonNull));
        assertTrue(Try.of(() -> { throw new Exception(); }).containsThrowable(Objects::nonNull));
        assertFalse(Try.of(() -> { throw new Exception(); }).containsThrowable(Objects::isNull));

        assertFalse(Try.of(() -> null).containsThrowable(Objects::isNull));
    }

    @Test
    void filter() {
        assertEquals(Try.of(() -> 0), Try.of(() -> 0).filter(x -> x==0));
        assertThrows(NoSuchElementException.class, () -> Try.of(() -> 1).filter(x -> x == 0).get());
        assertThrows(Exception.class, () -> Try.of(() -> { throw new Exception(); }).filter(x -> x.equals(new Object())).get());

        assertEquals(Try.of(() -> null), Try.of(() -> null).filter(Objects::isNull));
        assertThrows(NoSuchElementException.class, () -> Try.of(() -> null).filter(Objects::nonNull).get());
    }

    @Test
    void filterThrowable() {
        Exception e = new IndexOutOfBoundsException();
        assertEquals(Try.of(() -> 0), Try.of(() -> 0).filterThrowable(o -> o.equals(e)));
        assertThrows(IndexOutOfBoundsException.class, () -> Try.of(() -> { throw e; }).filterThrowable(o -> o.equals(e)).get());
        assertThrows(NoSuchElementException.class, () -> Try.of(() -> { throw new Exception(); }).filterThrowable(o -> o.equals(e)).get());

        assertEquals(Try.of(() -> null), Try.of(() -> null).filter(Objects::isNull));
        assertThrows(NoSuchElementException.class, () -> Try.of(() -> null).filter(Objects::nonNull).get());
    }

    @Test
    void then() {
    }

    @Test
    void testThen() {
    }

    @Test
    void testThen1() {
    }

    @Test
    void testThen2() {
    }

    @Test
    void testThen3() {
    }

    @Test
    void testThen4() {
    }

    @Test
    void recover() {
    }

    @Test
    void testRecover() {
    }

    @Test
    void failed() {
        assertThrows(UnsupportedOperationException.class, () -> Try.of(() -> 0).failed().get());
    }

    @Test
    void of() {
    }

    @Test
    void testOf() {
    }

}