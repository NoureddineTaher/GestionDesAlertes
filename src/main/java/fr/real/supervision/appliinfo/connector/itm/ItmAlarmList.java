package fr.real.supervision.appliinfo.connector.itm;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItmAlarmList {

	private String textContent = "";

	@JsonProperty("ROW")
	private Map<String, ItmAlarm> alarms;

	@XmlAttribute(name = "TextContent")
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	@XmlElement(name = "ROW")
	public Map<String, ItmAlarm> getAlarms() {
		return alarms == null ? new HashMap<>() : alarms;
	}

	public void setAlarms(Map<String, ItmAlarm> alarms) {
		this.alarms = alarms;
	}

	public ItmAlarmList(Map<String, ItmAlarm> alarms) {
		this.alarms = alarms;
	}

	public ItmAlarmList() {
	}

}
