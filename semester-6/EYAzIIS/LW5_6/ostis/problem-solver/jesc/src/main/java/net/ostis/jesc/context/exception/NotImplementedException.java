package net.ostis.jesc.context.exception;

public class NotImplementedException extends ScContextRuntimeException {
    /**
     * @param message - message about what went wrong
     */
    public NotImplementedException(String message) {
        super(message);
    }
}
