package org.altegox.common.exception;

public class AltegoException extends RuntimeException {

    public AltegoException() {
        super("altego-framework RuntimeException.");
    }

    public AltegoException(String message) {
        super(message);
    }

    public AltegoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AltegoException(Throwable cause) {
        super(cause);
    }

}