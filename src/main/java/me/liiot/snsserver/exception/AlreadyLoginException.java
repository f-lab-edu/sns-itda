package me.liiot.snsserver.exception;

public class AlreadyLoginException extends RuntimeException {

    public AlreadyLoginException() {
        super();
    }

    public AlreadyLoginException(String message) {
        super(message);
    }
}
