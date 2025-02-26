package org.altegox.common.exception;

public class ToolNotFindException extends AltegoException {

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