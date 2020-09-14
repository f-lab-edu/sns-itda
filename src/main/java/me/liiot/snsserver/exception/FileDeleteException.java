package me.liiot.snsserver.exception;

public class FileDeleteException extends RuntimeException {

    public FileDeleteException() {
        super();
    }

    public FileDeleteException(String message) {
        super(message);
    }

    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
