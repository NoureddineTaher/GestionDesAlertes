package fr.real.supervision.appliinfo.connector.sms;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsClientTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsClientTest.class);

	@Autowired
	private SmsClient smsClient;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void shouldSendSms() throws Exception {
		try {
			String[] phones = { "0682915582" };
			smsClient.send(phones, "Tout va bien");
		} catch (Exception e) {
			LOGGER.error("Il ne devrait pas y avoir d'exception ici", e);
			fail("Il ne devrait pas y avoir d'exception ici", e);
		}
	}

	@Test
	public void shouldSendAstreinte() throws Exception {
		try {
			smsClient.send("Tout va bien");
		} catch (Exception e) {
			LOGGER.error("Il ne devrait pas y avoir d'exception ici", e);
			fail("Il ne devrait pas y avoir d'exception ici", e);
		}
	}
	
	@Test
	public void shouldPing() {
		assertTrue("ping", smsClient.ping());
	}

}
