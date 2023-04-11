package fr.real.supervision.appliinfo.job.purge;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import fr.real.supervision.appliinfo.job.common.JobService;
import fr.real.supervision.appliinfo.model.JobEvent;
import fr.real.supervision.appliinfo.model.JobType;

public class PurgeJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(PurgeJob.class);

	@Autowired
	private JobService jobService;

	@Value("${jobs.purge.alertevent.retention.delay}")
	private Duration alertEventRetentionDelay;

	@Value("${jobs.purge.trace.retention.delay}")
	private Duration traceRetentionDelay;

	@Autowired
	private PurgeService purgeService;

	/**
	 * Tracer le démarrage du job en base
	 */
	private JobEvent setUp(JobEvent jobEvent) {
		jobEvent.setType(JobType.PURGE);
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

	@Scheduled(fixedDelayString = "${jobs.purge.delay}")
	public void process() {

		// Tracer l'éxécution du job en bdd
		JobEvent jobEvent = new JobEvent();
		setUp(jobEvent);

		LOGGER.info("Debut du job de purge des evenements d'id {}", jobEvent.getId());

		try {
			purgeService.purgeEvent(alertEventRetentionDelay);		
			purgeService.purgeTrace(traceRetentionDelay);
			jobEvent.setSuccess(Boolean.TRUE);
		} catch (Exception e) {
			LOGGER.error("Une erreur est survenue", e);
			jobEvent.setSuccess(Boolean.FALSE);
		}finally {			
			tearDown(jobEvent);
			LOGGER.info("Fin du job de purge des evenements d'id {}", jobEvent.getId());
		}
	}

}
