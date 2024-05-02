package net.ostis.jesc.context.exception;

/**
 * The most general runtime exception raised by ScContextCommon.
 *
 * @since 0.1.0
 */
public class ScContextRuntimeException extends RuntimeException {

    /**
     *
     * @param message - message about what went wrong
     */
    public ScContextRuntimeException(String message) {
        super(message);
    }

}
