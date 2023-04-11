package fr.real.supervision.appliinfo.connector.itm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.real.supervision.appliinfo.connector.itm.ItmAlarm;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmList;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmSeverity;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmSeverityList;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmTicket;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmTicketList;

public class XmlToBeanConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlToBeanConverter.class);

	public static ItmAlarmList convertXmlToItmAlarmList(Document doc) {

		Element element = doc.getDocumentElement();

		ItmAlarmList itmAlarmList = new ItmAlarmList();

		itmAlarmList.setTextContent(XmlUtils.getFirstLevelTextContent(element));

		List<Element> allElements = XmlUtils.getChildElements(element, "ROW");

		LOGGER.debug(String.format("XML contents %s entries", allElements.size()));

		Map<String, ItmAlarm> alarms = new HashMap<>();

		for (Element elem : allElements) {

			NodeList elemChildNodes = elem.getChildNodes();

			ItmAlarm itmAlarm = new ItmAlarm();

			for (int j = 0; j < elemChildNodes.getLength(); j++) {
				Node field = elemChildNodes.item(j);

				switch (field.getNodeName()) {
				case "HGBLTMSTMP":
					itmAlarm.setStart(field.getTextContent());
					break;
				case "HSITNAME":
					itmAlarm.setName(field.getTextContent());
					break;
				case "HDELTASTAT":
					itmAlarm.setStatus(field.getTextContent());
					break;
				case "HNODE":
					itmAlarm.setPasserelle(field.getTextContent());
					break;
				case "HORIGINNODE":
					itmAlarm.setServer(field.getTextContent());
					break;

				case "ATOMIZE":
					itmAlarm.setDisplayItem(field.getTextContent());
					break;


					default:
					break;
				}
			}

			alarms.put(itmAlarm.getName(), itmAlarm);

		}

		itmAlarmList.setAlarms(alarms);

		return itmAlarmList;
	}

	public static ItmAlarmSeverityList convertXmlToItmAlarmSeverityList(Document doc) {

		Element element = doc.getDocumentElement();

		ItmAlarmSeverityList itmAlarmSeverityList = new ItmAlarmSeverityList();

		itmAlarmSeverityList.setTextContent(XmlUtils.getFirstLevelTextContent(element));

		List<Element> allElements = XmlUtils.getChildElements(element, "ROW");

		LOGGER.debug(String.format("XML contents %s entries", allElements.size()));

		Map<String, ItmAlarmSeverity> severities = new HashMap<>();

		for (Element elem : allElements) {

			NodeList elemChildNodes = elem.getChildNodes();

			ItmAlarmSeverity itmAlarmSeverity = new ItmAlarmSeverity();

			for (int j = 0; j < elemChildNodes.getLength(); j++) {
				Node field = elemChildNodes.item(j);

				switch (field.getNodeName()) {
				case "HSITNAME":
					itmAlarmSeverity.setName(field.getTextContent());
					break;
				case "SITINFO":
					String[] fields = StringUtils.split(field.getTextContent(), ";");
					for (String s : fields) {
						String[] ss = StringUtils.split(s, '=');
						if ("SEV".equals(ss[0])) {
							itmAlarmSeverity.setValue(ss[1]);
						}
					}
					break;
				default:
					break;
				}
			}

			severities.put(itmAlarmSeverity.getName(), itmAlarmSeverity);

		}

		itmAlarmSeverityList.setAlarmsSeverities(severities);

		return itmAlarmSeverityList;
	}

	public static ItmAlarmTicketList convertXmlToItmAlarmTicketList(Document doc) {
		Element element = doc.getDocumentElement();

		ItmAlarmTicketList itmAlarmTicketList = new ItmAlarmTicketList();

		itmAlarmTicketList.setTextContent(XmlUtils.getFirstLevelTextContent(element));

		List<Element> allElements = XmlUtils.getChildElements(element, "ROW");

		LOGGER.debug(String.format("XML contents %s entries", allElements.size()));

		Map<String, ItmAlarmTicket> tickets = new HashMap<>();

		for (Element elem : allElements) {

			NodeList elemChildNodes = elem.getChildNodes();

			ItmAlarmTicket itmAlarmTicket = new ItmAlarmTicket();

			for (int j = 0; j < elemChildNodes.getLength(); j++) {
				Node field = elemChildNodes.item(j);

				switch (field.getNodeName()) {
				case "HSITNAME":
					itmAlarmTicket.setName(field.getTextContent());
					break;
				case "HDELTASTAT":
					itmAlarmTicket.setStatus(field.getTextContent());
					break;
				case "RESULTS":
					String fieldTextContent = field.getTextContent();
					if(fieldTextContent.contains("Reference ID associated with event. ID: "))
					{
						String firstPart = fieldTextContent.substring(fieldTextContent.indexOf(";REF=") + 5);
						itmAlarmTicket.setValue(firstPart.substring(0, firstPart.indexOf(';')));
						itmAlarmTicket.setDate(firstPart.substring(firstPart.indexOf("<b>")+3, firstPart.indexOf(" -")));
					}

				default:
					break;
				}
			}

			tickets.put(itmAlarmTicket.getName(), itmAlarmTicket);

		}

		itmAlarmTicketList.setAlarmsItmAlarmTickets(tickets);

		return itmAlarmTicketList;
	}
}
