package fr.real.supervision.appliinfo.connector.sms;

public class SmsException extends Exception {

	public SmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmsException(String message) {
		super(message);
	}

	public SmsException() {
		super();
	}

	public SmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SmsException(Throwable cause) {
		super(cause);
	}

}
