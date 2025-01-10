package org.rangenx.common.exception;

public class ToolNotFindException extends RangenException {

    public ToolNotFindException() {
        super("Tool not found.");
    }

    public ToolNotFindException(String message) {
        super(message);
    }

    public ToolNotFindException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolNotFindException(Throwable cause) {
        super(cause);
    }

}