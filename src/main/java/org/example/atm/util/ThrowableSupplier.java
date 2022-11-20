package org.example.atm.util;

public interface ThrowableSupplier<T> {
    T get() throws Exception;
}