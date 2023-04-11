package fr.real.supervision.appliinfo.connector.itm;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItmAlarmTicketList {

	private String textContent = "";

	@JsonProperty("ROW")
	private Map<String, ItmAlarmTicket> alarmTickets;

	@XmlElement(name = "ROW")
	public Map<String, ItmAlarmTicket> getAlarmItmAlarmTickets() {
		return this.alarmTickets == null ? new HashMap<>() : this.alarmTickets;
	}

	public void setAlarmsItmAlarmTickets(Map<String, ItmAlarmTicket> alarmTickets) {
		this.alarmTickets = alarmTickets;
	}

	@XmlAttribute(name = "TextContent")
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

}
