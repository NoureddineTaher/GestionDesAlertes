package fr.real.supervision.appliinfo.exception;

public class PlatformException extends Exception {

	public PlatformException(final String message) {
		super(message);
	}
	
	public PlatformException(final Throwable cause) {
		super(cause);
	}
	
	public PlatformException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
