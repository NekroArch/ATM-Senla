package org.example.exception;

public class ATMWorkException extends RuntimeException {
    public ATMWorkException(String message) {
        super(message);
    }
    public ATMWorkException(ATMWorkException sourceException) {
        super(sourceException);
    }

}
