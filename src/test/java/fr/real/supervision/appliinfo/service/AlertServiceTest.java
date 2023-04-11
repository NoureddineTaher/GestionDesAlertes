package fr.real.supervision.appliinfo.service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.real.supervision.appliinfo.connector.itm.ItmAlarm;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmList;
import fr.real.supervision.appliinfo.connector.itm.ItmClient;
import fr.real.supervision.appliinfo.job.alert.AlertService;

// FIXME utiliser mockserver ou wire mock pour mocker les appels ITM
//@RunWith(MockitoJunitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertServiceTest {
	@Autowired
	private AlertService alertService;

	@MockBean
	private ItmClient itmClient;

	@Before
	public void setUp() throws Exception {
		File resource = new ClassPathResource("itm/response-prod.xml").getFile();
		String itmResponse = new String(Files.readAllBytes(resource.toPath()));

		ItmClient itmClientFromContext = new ItmClient();
		ItmAlarmList itmAlarms = itmClientFromContext.parseXmlAlarmList(itmResponse);
		Mockito.when(itmClient.fetchAlarms()).thenReturn(itmAlarms).thenReturn(new ItmAlarmList());
	}

	@Test
	public void shouldProcessAlert() throws Exception {
		alertService.consolidateAlertEvents(new ArrayList<ItmAlarm>(itmClient.fetchAlarms().getAlarms().values()));
	}
}
