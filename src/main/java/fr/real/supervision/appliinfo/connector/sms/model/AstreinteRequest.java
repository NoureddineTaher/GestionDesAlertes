package fr.real.supervision.appliinfo.connector.sms.model;

import java.util.UUID;

public class AstreinteRequest {

	private String id;

	private String content;
	
	public AstreinteRequest(String content) {
		id = UUID.randomUUID().toString();
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "AstreinteRequest [id=" + id + ", content=" + content + "]";
	}

}
