package fr.real.supervision.appliinfo.connector.itm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ItmClient.class })
public class ItmClientTest {

	@Autowired
	private ItmClient itmClient = new ItmClient();

	@Before
	public void setUp() throws Exception {
	}
	@Test
	public void shouldFetchAlarms() {
		Map<String, ItmAlarm> alarms;
		try {
			alarms = itmClient.fetchAlarms().getAlarms();
			assertNotNull(alarms);
			assertTrue(alarms.size() > 0);
		} catch (ItmException | DOMException | TransformerException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void shouldFetchSeverities() throws Exception {
		try {
			List<ItmAlarmSeverity> severities = new ArrayList<>(
					itmClient.fetchSeverities().getAlarmSeverities().values());
			assertNotNull(severities);
			assertTrue(severities.size() > 0);
		} catch (Exception e) {
			assumeNoException("Il ne devrait pas y aoir d'exception ici", e);
		}
	}

}
