package me.liiot.snsserver.exception;

public class NotExistUserIdException extends RuntimeException {

    public NotExistUserIdException() {
        super();
    }

    public NotExistUserIdException(String message) {
        super(message);
    }
}
