package org.example.exception;

public class InvalidCardException extends ATMWorkException{

    public InvalidCardException(String message) {
        super(message);
    }
}
