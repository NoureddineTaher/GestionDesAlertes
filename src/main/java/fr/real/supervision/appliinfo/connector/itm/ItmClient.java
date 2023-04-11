package fr.real.supervision.appliinfo.connector.itm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.real.supervision.appliinfo.connector.itm.utils.XmlToBeanConverter;
import fr.real.supervision.appliinfo.connector.itm.utils.XmlUtils;

@Service
@EnableConfigurationProperties(ItmProperties.class)
public class ItmClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItmClient.class);

	@Autowired
	ItmProperties properties;

	public ItmAlarmList fetchAlarms()
			throws ItmException, DOMException, TransformerException, ParserConfigurationException, SAXException {
		// @formatter:off
		String xmlInput = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>" 
				+ "<SOAP-ENV:Body>"
							+ "<CT_Get>" 
								+ "<userid>" + properties.getUser() + "</userid>" 
								+ "<password>" + properties.getPassword() + "</password>" 
								+ "<table>O4SRV.ISITSTSH</table>"
								+ "<sql>SELECT ATOMIZE, GBLTMSTMP, SITNAME, DELTASTAT, NODE, ORIGINNODE FROM O4SRV.ISITSTSH WHERE DELTASTAT='Y' OR DELTASTAT='A' OR DELTASTAT='E' ORDER BY GBLTMSTMP</sql>"
							+ "</CT_Get>" 
				+ "</SOAP-ENV:Body>" 
			+ "</SOAP-ENV:Envelope>";
		// @formatter:on
		ItmAlarmList alarms = null;
		try {
			String response = callItmSoapService("", xmlInput);
			alarms = parseXmlAlarmList(response);
			Map<String, ItmAlarmSeverity> severities = fetchSeverities().getAlarmSeverities();
			Map<String, ItmAlarm> alarmsToBeUpdated = alarms.getAlarms();
			Set<String> severitiesKeyset = severities.keySet();
			severitiesKeyset.forEach(key -> {
				if (alarmsToBeUpdated.containsKey(key)) {
					alarmsToBeUpdated.get(key).setSeverite(severities.get(key).getValue());
					LOGGER.debug(alarmsToBeUpdated.get(key).toString());
				}
			});

			alarms.setAlarms(alarmsToBeUpdated);

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new ItmException(e);
		}
		return alarms;
	}

	/**
	 * Transforme une reponse itm en liste d'alarmes itm
	 * 
	 * @param in
	 * @return
	 * @throws ItmException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws DOMException
	 */
	public ItmAlarmList parseXmlAlarmList(String in) {

		ItmAlarmList alarms = null;
		try {
			String extracted = StringUtils.substring(in, StringUtils.indexOfIgnoreCase(in, "<DATA>"),
					StringUtils.lastIndexOfIgnoreCase(in, "</DATA>") + 7);

			Document doc = XmlUtils.createDocument(extracted);

			alarms = XmlToBeanConverter.convertXmlToItmAlarmList(doc);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return alarms;
	}

	public ItmAlarmSeverityList fetchSeverities() throws ItmException {

		// @formatter:off
				String xmlInput = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
						+ "<soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>" 
						+ "<SOAP-ENV:Body>"
									+ "<CT_Get>" 
										+ "<userid>" + properties.getUser() + "</userid>" 
										+ "<password>" + properties.getPassword() + "</password>" 
										+ "<table>O4SRV.ISITSTSH</table>"
										+ "<sql>SELECT SITNAME, SITINFO FROM O4SRV.TSITDESC</sql>"
									+ "</CT_Get>" 
						+ "</SOAP-ENV:Body>" 
					+ "</SOAP-ENV:Envelope>";
				// @formatter:on
		ItmAlarmSeverityList severities = null;
		try {
			String response = callItmSoapService("", xmlInput);

			String extracted = StringUtils.substring(response, StringUtils.indexOfIgnoreCase(response, "<DATA>"),
					StringUtils.lastIndexOfIgnoreCase(response, "</DATA>") + 7);

			LOGGER.trace(extracted);

			Document doc = XmlUtils.createDocument(extracted);

			severities = XmlToBeanConverter.convertXmlToItmAlarmSeverityList(doc);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ItmException(e);
		}
		return severities;
	}

	public ItmAlarmTicketList fetchTickets() throws ItmException {

		// @formatter:off
				String xmlInput = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
						+ "<soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>" 
						+ "<SOAP-ENV:Body>"
									+ "<CT_Get>" 
										+ "<userid>" + properties.getUser() + "</userid>" 
										+ "<password>" + properties.getPassword() + "</password>" 
										+ "<table>O4SRV.ISITSTSH</table>"
										+ "<sql>SELECT GBLTMSTMP, SITNAME, DELTASTAT, NODE, ORIGINNODE, RESULTS FROM O4SRV.TSITSTSH WHERE DELTASTAT='Y' OR DELTASTAT='A' OR DELTASTAT='E' ORDER BY SITNAME,GBLTMSTMP</sql>"
									+ "</CT_Get>" 
						+ "</SOAP-ENV:Body>" 
					+ "</SOAP-ENV:Envelope>";
				// @formatter:on
		ItmAlarmTicketList tickets = null;
		try {
			String response = callItmSoapService("", xmlInput);

			String extracted = StringUtils.substring(response, StringUtils.indexOfIgnoreCase(response, "<DATA>"),
					StringUtils.lastIndexOfIgnoreCase(response, "</DATA>") + 7);

			LOGGER.trace(extracted);

			Document doc = XmlUtils.createDocument(extracted);

			tickets = XmlToBeanConverter.convertXmlToItmAlarmTicketList(doc);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ItmException(e);
		}
		return tickets;
	}

	/**
	 * 
	 * @param soapAction
	 * @param soapEnvelope
	 * @return
	 * @throws IOException
	 * @throws ItmException
	 */
	protected String callItmSoapService(String soapAction, String soapEnvelope) throws IOException, ItmException {
		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		URL url = new URL(properties.getUrl());
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		byte[] buffer;
		buffer = soapEnvelope.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", soapAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		// Write the content of the request to the outputstream of the HTTP Connection.
		out.write(b);
		out.close();
		// Ready with sending the request.
		int statusCode = httpConn.getResponseCode();

		if (statusCode != 200) {
			LOGGER.debug("Status code: {} ", statusCode);
			throw new ItmException("Unexpected response status: " + statusCode);
		}

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Write the SOAP message response to a String.
		StringBuilder outputStringBuilder = new StringBuilder();
		while ((responseString = in.readLine()) != null) {
			outputStringBuilder.append(responseString);
		}

		outputString = outputStringBuilder.toString();

		if (outputString.toLowerCase().contains("soapfault")) {
			throw new ItmException("soapfault response : " + outputString);
		}

		LOGGER.trace("reponse [{}]", outputString);

		return outputString;

	}

}
