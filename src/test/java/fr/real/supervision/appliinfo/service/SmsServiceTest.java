package fr.real.supervision.appliinfo.service;

import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.real.supervision.appliinfo.job.sms.SmsService;
import fr.real.supervision.appliinfo.model.AlertEvent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsServiceTest {

	@Autowired
	private SmsService smsService;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void shouldSendSms() {
		try {
			List<AlertEvent> lastAlertEvents = smsService.getLastAlertEvents();

			for (AlertEvent alertEvent : lastAlertEvents) {
				smsService.sendSmsForAlertEvent(alertEvent, Duration.ofMillis(1));
			}
			
		} catch (Exception e) {
			fail("Pas d'exception attendue ici et pourtant", e);
		}

	}

}
