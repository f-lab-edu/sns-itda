package me.liiot.snsserver.exception;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException() {
        super();
    }

    public InvalidValueException(String message) {
        super(message);
    }
}
