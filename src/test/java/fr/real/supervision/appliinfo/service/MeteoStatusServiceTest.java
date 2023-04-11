package fr.real.supervision.appliinfo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import fr.real.supervision.appliinfo.web.status.config.MeteoStatusDto;
import fr.real.supervision.appliinfo.web.status.service.MeteoStatusService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = { "dev", "appliinfo" })
public class MeteoStatusServiceTest {

	@Autowired
	private MeteoStatusService service;

	@Test
	public void serviceShouldReturnConfiguration() {
		List<MeteoStatusDto> config = this.service.getData();
		assertThat(config).hasSizeGreaterThan(0);
	}
}
