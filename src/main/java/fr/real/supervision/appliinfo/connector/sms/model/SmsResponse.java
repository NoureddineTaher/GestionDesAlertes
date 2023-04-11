package fr.real.supervision.appliinfo.connector.sms.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SmsResponse {

	private boolean success;

	private DbStatus dbStatus;

	private SmsStatus smsStatus;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public SmsStatus getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(SmsStatus smsStatus) {
		this.smsStatus = smsStatus;
	}

	public void setDbStatus(DbStatus dbStatus) {
		this.dbStatus = dbStatus;
	}

	public DbStatus getDbStatus() {
		return dbStatus;
	}

}
