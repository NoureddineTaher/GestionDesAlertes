package fr.real.supervision.appliinfo.connector.itm;

import javax.xml.bind.annotation.XmlAttribute;

public class ItmAlarmSeverity {

	private String name;
	private String value;

	@XmlAttribute(name = "HSITNAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "SITINFO")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ItmAlarmSeverity(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public ItmAlarmSeverity() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItmAlarmSeverity [\n\tname=");
		builder.append(name);
		builder.append("\n\t, value=");
		builder.append(value);
		builder.append("\n]");
		return builder.toString();
	}

}
