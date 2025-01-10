package org.rangenx.common.exception;

public class RepeatInitializeException extends RangenException {

    public RepeatInitializeException() {
        super("rangen-framework has already been initialized and cannot be initialized again.");
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