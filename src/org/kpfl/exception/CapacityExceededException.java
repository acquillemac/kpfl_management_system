package org.kpfl.exception;

public class CapacityExceededException extends KPFLException {
    private static final long serialVersionUID = 1L;

    public CapacityExceededException(String message) {
        super(message);
    }
}
