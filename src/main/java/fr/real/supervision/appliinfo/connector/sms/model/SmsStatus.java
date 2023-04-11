package fr.real.supervision.appliinfo.connector.sms.model;

public class SmsStatus {

	private Result result;

	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
