package org.rangenx.common.exception;

public class RangenxException extends RuntimeException {

    public RangenxException() {
        super("rangenx-framework RuntimeException.");
    }

    public RangenxException(String message) {
        super(message);
    }

    public RangenxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangenxException(Throwable cause) {
        super(cause);
    }

}