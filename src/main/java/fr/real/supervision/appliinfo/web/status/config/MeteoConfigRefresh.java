package fr.real.supervision.appliinfo.web.status.config;

import java.util.Collection;

import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class MeteoConfigRefresh {

	private final RefreshEndpoint refreshEndpoint;
	private final MeteoStatusConfigProps properties;

	@Scheduled(cron = "${config.refresh.rate}")
	public void fireRefreshEvent() {
		log.info("Refresh Services statuses configuration...");
		Collection<String> refreshed = refreshEndpoint.refresh();
	}

}
