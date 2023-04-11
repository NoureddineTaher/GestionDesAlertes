package fr.real.supervision.appliinfo.web.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import fr.real.supervision.appliinfo.web.HomeController;

/**
 * In this test, the full Spring application context is started with the server
 * listening at a random port. Use TestRestTemplate for client-side tests.
 * 
 * @author tehdy.draoui
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SmokeTest_IT {

	@LocalServerPort
	private int port;

	@Autowired
	private HomeController homeController;

	@Autowired
	private TestRestTemplate restTemplate;

	@TestConfiguration
	static class Config {
		@Bean
		RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
					.setReadTimeout(Duration.ofSeconds(1));
		}
	}

	@Test
	public void homeControllerShoudBeLoaded() throws Exception {
		// then
		assertThat(homeController).isNotNull();
	}

	@Test
	public void whenRequestAppUrlOnServerPortThenShowHomePage() throws Exception {
		// when
		String body = restTemplate.getForObject("http://localhost:" + port + "/", String.class);

		// then
		assertThat(body.contains(HomeController.HOME_TITLE));
	}
}
