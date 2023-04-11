package fr.real.supervision.appliinfo.connector.sms.model;

import java.util.UUID;

public class SmsRequest {

	private String id;

	private String to;

	private String content;
	
	public SmsRequest(String to, String content) {
		id = UUID.randomUUID().toString();
		this.to = to;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "SmsRequest [id=" + id + ", to=" + to + ", content=" + content + "]";
	}
}
