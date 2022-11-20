package org.example.exception;

public class InvalidAmountException extends ATMWorkException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
