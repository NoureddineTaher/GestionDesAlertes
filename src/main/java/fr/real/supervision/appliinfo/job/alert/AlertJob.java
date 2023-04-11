package fr.real.supervision.appliinfo.job.alert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import fr.real.supervision.appliinfo.connector.itm.ItmAlarm;
import fr.real.supervision.appliinfo.connector.itm.ItmClient;
import fr.real.supervision.appliinfo.connector.itm.ItmException;
import fr.real.supervision.appliinfo.job.common.JobService;
import fr.real.supervision.appliinfo.model.JobEvent;
import fr.real.supervision.appliinfo.model.JobType;

public class AlertJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertJob.class);

	@Autowired
	private ItmClient itmClient;

	@Autowired
	private JobService jobService;

	@Autowired
	private AlertService alertService;

	/**
	 * Tracer le démarrage du job en base
	 */
	private JobEvent setUp(JobEvent jobEvent) {
		jobEvent.setType(JobType.ALERT);
		jobEvent.setStart(LocalDateTime.now());
		return jobService.save(jobEvent);
	}

	/**
	 * Tracer la fin du job en base
	 */
	private JobEvent tearDown(JobEvent jobEvent) {
		jobEvent.setEnd(LocalDateTime.now());
		return jobService.save(jobEvent);
	}

	@Scheduled(fixedDelayString = "${jobs.alert.delay}")
	public void process() {

		// Tracer l'éxécution du job en bdd
		JobEvent jobEvent = new JobEvent();
		setUp(jobEvent);

		LOGGER.info("Debut du job de consolidation des alarmes d'id {}", jobEvent.getId());

		try {
			// Récupérer les alarmes itm
			List<ItmAlarm> itmAlarms = new ArrayList<ItmAlarm>(itmClient.fetchAlarms().getAlarms().values());

			// En déduire les nouveaux évènements d'alerte et les persister
			alertService.consolidateAlertEvents(itmAlarms);

			jobEvent.setSuccess(Boolean.TRUE);
		} catch (ItmException e) {
			LOGGER.error("Erreur lors de la recuperation des alarmes ITMs", e);
			jobEvent.setSuccess(Boolean.FALSE);
		} catch (Exception e) {
			LOGGER.error("Une erreur est survenue", e);
			jobEvent.setSuccess(Boolean.FALSE);
		} finally {
			tearDown(jobEvent);
			LOGGER.info("Fin du job de consolidation des alarmes d'id {}", jobEvent.getId());
		}
	}

}
