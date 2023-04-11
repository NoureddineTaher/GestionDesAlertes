package fr.real.supervision.appliinfo.job.astreinte;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import fr.real.supervision.appliinfo.job.common.JobService;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.model.JobEvent;
import fr.real.supervision.appliinfo.model.JobType;

// TODO construire un rapport de job (nombre de astreinte envoyé, nombre d'erreur)
public class AstreinteJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstreinteJob.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private AstreinteService astreinteService;

    @Value("${jobs.astreinte.sending.latency}")
    private Duration sendingLatency;

    /**
     * Tracer le démarrage du job en base
     */
    private JobEvent setUp(JobEvent jobEvent) {
        jobEvent.setType(JobType.ASTREINTE);
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

    /**
     * On utilise un fixedDelay plutot qu'une expression cron ou qu'un fixedRate
     * pour n'avoir qu'un seul job à la fois qui tourne et ne pas envoyer plusieurs
     * astreinte pour une meme alerte
     */
    @Scheduled(fixedDelayString = "${jobs.astreinte.delay}")
    public void process() {

        int nbError = 0;

        // Tracer l'éxécution du job en bdd
        JobEvent jobEvent = new JobEvent();
        setUp(jobEvent);

        LOGGER.info("Debut du job d'envoi de astreinte d'id {}", jobEvent.getId());

        try {
            List<AlertEvent> lastAlertEvents = astreinteService.getLastAlertEvents();

            for (AlertEvent alertEvent : lastAlertEvents) {
                try {
                    // if no maintenance
                    if (alertEvent.getAlert().isActive()) {
                        astreinteService.sendAstreinteForAlertEvent(alertEvent, sendingLatency);
                    } else {
                        LOGGER.error("Astreinte de l'alerte passee pour cause de maintenance : {}",
                                alertEvent.getAlert().getName());
                    }
                } catch (Exception e) {
                    nbError++;
                    LOGGER.error("Erreur d'envoi de sms pour astreinte", e);
                }
            }

            jobEvent.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenue", e);
            jobEvent.setSuccess(Boolean.FALSE);
        } finally {
            tearDown(jobEvent);
            LOGGER.info("Fin du job d'envoi de astreinte d'id {} avec {} erreurs ", jobEvent.getId(), nbError);
        }

    }

}