package org.kpfl.exception;

public class SchedulingConflictException extends KPFLException {
    private static final long serialVersionUID = 1L;

    public SchedulingConflictException(String message) {
        super(message);
    }
}
