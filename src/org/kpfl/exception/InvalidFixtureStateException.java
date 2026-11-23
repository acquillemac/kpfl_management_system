package org.kpfl.exception;

public class InvalidFixtureStateException extends KPFLException {
    private static final long serialVersionUID = 1L;

    public InvalidFixtureStateException(String message) {
        super(message);
    }
}
