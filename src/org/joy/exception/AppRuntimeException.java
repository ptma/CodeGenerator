package org.joy.exception;

public class AppRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5632166483086724421L;

    public AppRuntimeException(){
    }

    public AppRuntimeException(Throwable cause){
        super(cause);
    }

    public AppRuntimeException(String message){
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

}
