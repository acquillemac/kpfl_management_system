package org.kpfl.exception;

public class InvalidValueException extends KPFLException {
    private static final long serialVersionUID = 1L;

    public InvalidValueException(String message) {
        super(message);
    }
}
