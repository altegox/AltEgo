package org.rangenx.common.exception;

public class RangenException extends RuntimeException {

    public RangenException() {
        super("rangen-framework RuntimeException.");
    }

    public RangenException(String message) {
        super(message);
    }

    public RangenException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangenException(Throwable cause) {
        super(cause);
    }

}