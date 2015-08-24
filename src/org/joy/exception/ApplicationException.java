package org.joy.exception;

public class ApplicationException extends Exception {

    private static final long serialVersionUID = -4441995852407613175L;

    public ApplicationException() {
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
