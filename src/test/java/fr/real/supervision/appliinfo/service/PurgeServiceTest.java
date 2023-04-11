package fr.real.supervision.appliinfo.service;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.real.supervision.appliinfo.job.purge.PurgeService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PurgeServiceTest {

	@Autowired
	private PurgeService purgeService;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void shouldPurgeEvent() throws Exception {
		purgeService.purgeEvent(Duration.ofMillis(1L));
	}

	@Test
	public void shouldPurgeTrace() throws Exception {
		purgeService.purgeTrace(Duration.ofMillis(1L));
	}
	
}
