package org.kpfl.exception;

/**
 * Base exception for all Kingston Premier Football League business rule violations.
 */
public class KPFLException extends Exception {
    private static final long serialVersionUID = 1L;

    public KPFLException(String message) {
        super(message);
    }

    public KPFLException(String message, Throwable cause) {
        super(message, cause);
    }
}
