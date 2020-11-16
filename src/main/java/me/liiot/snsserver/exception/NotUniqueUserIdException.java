package me.liiot.snsserver.exception;

public class NotUniqueUserIdException extends RuntimeException {

    public NotUniqueUserIdException(String message) {
        super(message);
    }
}
