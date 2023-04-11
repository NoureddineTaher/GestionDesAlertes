package fr.real.supervision.appliinfo.connector.itm;

public class ItmException extends Exception {

	public ItmException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItmException(String message) {
		super(message);
	}

	public ItmException() {
		super();
	}

	public ItmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ItmException(Throwable cause) {
		super(cause);
	}

}
