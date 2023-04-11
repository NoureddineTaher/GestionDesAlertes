package fr.real.supervision.appliinfo.job.astreinte;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import fr.real.supervision.appliinfo.repository.HolidayRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fr.real.supervision.appliinfo.connector.sms.SmsClient;
import fr.real.supervision.appliinfo.connector.sms.SmsException;
import fr.real.supervision.appliinfo.exception.FunctionalException;
import fr.real.supervision.appliinfo.exception.PlatformException;
import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import fr.real.supervision.appliinfo.repository.AlertRepository;

@Service
@Transactional
public class AstreinteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstreinteService.class);

    @Autowired
    private AlertEventRepository alertEventRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private TemplateEngine astreinteTemplateEngine;

    /**
     * Retourne la liste du dernier évènement de toutes les alertes configurées
     *
     * @return la liste du dernier évènement de toutes les alertes configurées
     */
    // TODO A factoriser avec le service d'envoi de sms ou à rendre plus specialisé
    // pour les sms
    public List<AlertEvent> getLastAlertEvents() {
        List<AlertEvent> lastAlertEvents = new ArrayList<>();
        for (Alert alert : alertRepository.findAll()) {
            AlertEvent event = alertEventRepository.findFirstByAlertOrderByStartDesc(alert);
            if (event != null) {
                lastAlertEvents.add(event);
            }
        }
        return lastAlertEvents;
    }

    /**
     * Envoi d'un sms de début d'alerte
     *
     * @param alertEvent
     * @throws PlatformException
     * @throws FunctionalException
     */
    private void sendAstreinte(AlertEvent alertEvent) throws PlatformException, FunctionalException {

        // Reattacher l'alertEvent pour éviter une lazy loading exception
        alertEvent = alertEventRepository.findById(alertEvent.getId()).orElseThrow(() -> new FunctionalException(
                "Tentative d'envoie de sms pour une événement d'alerte qui n'est plus présent en base"));

        // Contruire le message à envoyer
        String message;

        //Si astreinte personnalisée vide: construction avec le template classique
        if (StringUtils.isBlank(alertEvent.getAlert().getAstreinte())) {
            Context context = new Context();
            context.setVariable("alertEvent", alertEvent);
            message = astreinteTemplateEngine.process("astreinteTemplate", context);

        } else { // Sinon construction avec l'astreinte personnalisée et remplacement des variables
            message = alertEvent.getAlert().getAstreinte().replace("${alertEvent.alert.name}", alertEvent.getAlert().getName());
            message = message.replace("${alertEvent.alert.alarms}", StringUtils.join(alertEvent.getAlarmEvents(), " | "));
        }

        // Envoyer le sms d'astreinte
        try {
            smsClient.send(message);
        } catch (SmsException e) {
            throw new PlatformException(e);
        }

    }

    public void sendAstreinteForAlertEvent(AlertEvent alertEvent, TemporalAmount sendingLatency) {
        switch (alertEvent.getStatus()) {
            case STARTED:
            case CONFIRMED:
                if (alertEvent.getAstreinteSent() == null
                        && alertEvent.getStart().isBefore(LocalDateTime.now().minus(sendingLatency))
                        && alertEvent.getAlert().getHoursWhenAstreinteIsAllowedList().stream()
                        .anyMatch(workingHour -> (
                                (holidayRepository.existsByDay(LocalDate.now()) &&
                                        workingHour.isSameDay(LocalDateTime.now())) ||
                                        workingHour.contains(LocalDateTime.now()))
                        )) {
                    try {
                        LOGGER.info("Envoi d'un SMS d'astreinte pour l'alerte {}", alertEvent.getAlert().getName());
                        sendAstreinte(alertEvent);
                        // Tracer l'envoi de astreinte
                        alertEvent.setAstreinteSent(LocalDateTime.now());
                        alertEventRepository.save(alertEvent);
                    } catch (PlatformException | FunctionalException e) {
                        LOGGER.error("Le sms d'astreinte de l'alertEvent " + alertEvent.getId() + " n'a pas ete envoye", e);
                    }
                }
                break;
            default:
                break;
        }
    }

}
