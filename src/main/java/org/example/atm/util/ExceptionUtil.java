package org.example.atm.util;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static <T> T wrapException(ThrowableSupplier<T> runnable) {
        try {
            return runnable.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
