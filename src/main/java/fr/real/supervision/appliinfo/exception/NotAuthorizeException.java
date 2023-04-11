package fr.real.supervision.appliinfo.exception;

public class NotAuthorizeException extends RuntimeException {
    public NotAuthorizeException(String errorMessage) {
        super(errorMessage);
    }
}
