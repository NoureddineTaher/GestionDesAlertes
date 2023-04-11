package fr.real.supervision.appliinfo.connector.itm;

import javax.xml.bind.annotation.XmlAttribute;

public class ItmAlarmTicket {

	private String name;
	private String status;
	private String value;
	private String date;

	@XmlAttribute(name = "HSITNAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "HDELTASTAT")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlAttribute(name = "RESULTS")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ItmAlarmTicket(String name, String status, String value,String date) {
		this.name = name;
		this.status = status;
		this.value = value;
		this.date = date;
	}

	public ItmAlarmTicket() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItmAlarmTicket [name=");
		builder.append(name);
		builder.append(", status=");
		builder.append(status);
		builder.append(", value=");
		builder.append(value);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

}
