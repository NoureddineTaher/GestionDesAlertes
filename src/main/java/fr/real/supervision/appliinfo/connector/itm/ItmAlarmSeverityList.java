package fr.real.supervision.appliinfo.connector.itm;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItmAlarmSeverityList {

	private String textContent = "";

	@JsonProperty("ROW")
	private Map<String, ItmAlarmSeverity> alarmSeverities;

	@XmlElement(name = "ROW")
	public Map<String, ItmAlarmSeverity> getAlarmSeverities() {
		return this.alarmSeverities == null ? new HashMap<>() : this.alarmSeverities;
	}

	public void setAlarmsSeverities(Map<String, ItmAlarmSeverity> alarmSeverities) {
		this.alarmSeverities = alarmSeverities;
	}

	@XmlAttribute(name = "TextContent")
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

}
