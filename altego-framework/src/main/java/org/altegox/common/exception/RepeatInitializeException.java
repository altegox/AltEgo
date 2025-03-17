package org.altegox.common.exception;

public class RepeatInitializeException extends AltegoException {

    public RepeatInitializeException() {
        super("altego-framework has already been initialized and cannot be initialized again.");
    }

    public RepeatInitializeException(String message) {
        super(message);
    }

    public RepeatInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatInitializeException(Throwable cause) {
        super(cause);
    }

}