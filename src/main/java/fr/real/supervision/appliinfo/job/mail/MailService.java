package fr.real.supervision.appliinfo.job.mail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fr.real.supervision.appliinfo.connector.mail.MailClient;
import fr.real.supervision.appliinfo.exception.FunctionalException;
import fr.real.supervision.appliinfo.exception.PlatformException;
import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import fr.real.supervision.appliinfo.repository.AlertRepository;
import fr.real.supervision.appliinfo.repository.HolidayRepository;

@Service
@Transactional
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private static final String START_SUBJECT_TEMPLATE = "startSubjectTemplate";

    @Autowired
    private AlertEventRepository alertEventRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine emailTemplateEngine;

    /**
     * Retourne la liste du dernier évènement de toutes les alertes configurées
     *
     * @return la liste du dernier évènement de toutes les alertes configurées
     */
    // TODO A factoriser avec le service d'envoi de sms ou à rendre plus specialisé
    // pour les mail
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
     * Envoi d'un mail de début d'alerte
     *
     * @param alertEvent
     * @throws PlatformException
     * @throws FunctionalException
     */
    private void sendMail(AlertEvent alertEvent, String subjectTemplate, String bodyTemplate)
            throws PlatformException, FunctionalException {

        // Reattacher l'alertEvent pour éviter une lazy loading exception
        alertEvent = alertEventRepository.findById(alertEvent.getId()).orElseThrow(() -> new FunctionalException(
                "Tentative d'envoie de mail pour une événement d'alerte qui n'est plus présent en base"));

        // Construire le mail à envoyer
        String subject;
        String content;

        // Si 1er champ du mail personnalisé vide, contruction avec le template
        // classique
        if (StringUtils.isBlank(alertEvent.getAlert().getStartSubject())) {
            Context context = new Context();
            context.setVariable("alertEvent", alertEvent);
            subject = emailTemplateEngine.process(subjectTemplate, context);
            content = emailTemplateEngine.process(bodyTemplate, context);

        } else { // Sinon contruction avec le mail personnalisé et remplacement des variables

            // Différenciation des cas de début ou fin d'alerte
            if (subjectTemplate.equals(START_SUBJECT_TEMPLATE)) {
                subject = alertEvent.getAlert().getStartSubject();
                content = alertEvent.getAlert().getStartBody();
            } else {
                subject = alertEvent.getAlert().getEndSubject();
                content = alertEvent.getAlert().getEndBody();
            }

            subject = subject.replace("${alertEvent.alert.name}", alertEvent.getAlert().getName());

            content = content.replace("${alertEvent.alert.name}", alertEvent.getAlert().getName());
            content = content.replace("${alertEvent.alert.alarms}",
                    StringUtils.join(alertEvent.getAlarmEvents(), " | "));
            content = content.replace("${alertEvent.start}", alertEvent.getStart().toString());
            content = content.replace("${alertEvent.weightSum}", alertEvent.getWeightSum().toString());
            content = content.replace("${alertEvent.lastUpdate}", alertEvent.getLastUpdate().toString());
        }

        // Trouver tous les contacts à qui envoyer le mail
        List<Contact> contacts = alertEvent.getAlert().getDiffusionGroups().stream()
                .flatMap(diffusionGroup -> diffusionGroup.getContacts().stream()).distinct()
                .collect(Collectors.toList());
        String[] emailAdresses = contacts.stream().map(Contact::getEmail).toArray(String[]::new);

        // Envoyer le mail
        mailClient.prepareAndSend("appliinfo@intranet-adsn.fr", emailAdresses, subject, content);

    }

    public void sendMailForAlertEvent(AlertEvent alertEvent, TemporalAmount sendingLatency)
            throws PlatformException, FunctionalException {
        switch (alertEvent.getStatus()) {
            case STARTED, CONFIRMED:
                if (alertEvent.getStartMailSent() == null
                        && alertEvent.getStart().isBefore(LocalDateTime.now().minus(sendingLatency))
                        && alertEvent.getAlert().getHoursWhenMailIsAllowed().stream()
                        .anyMatch(workingHour -> ((holidayRepository.existsByDay(LocalDate.now())
                                && workingHour.isSameDay(LocalDateTime.now()))
                                || workingHour.contains(LocalDateTime.now())))) {

                    LOGGER.info("Envoi de mail de début pour l'alerte {}", alertEvent.getAlert().getName());
                    sendMail(alertEvent, START_SUBJECT_TEMPLATE, "startBodyTemplate");
                    // Tracer l'envoi de mail
                    alertEvent.setStartMailSent(LocalDateTime.now());
                    alertEventRepository.save(alertEvent);

                }
                break;
            case FINISHED:
                if (alertEvent.getStartMailSent() != null && alertEvent.getEndMailSent() == null) {

                    // RG : le mail finished est envoyé quelque soit l'heure
                    LOGGER.info("Envoi de mail de Fin pour l'alerte {}", alertEvent.getAlert().getName());
                    sendMail(alertEvent, "endSubjectTemplate", "endBodyTemplate");
                    alertEvent.setEndMailSent(LocalDateTime.now());
                    alertEventRepository.save(alertEvent);

                }
                break;

            default:
                break;
        }
    }
}
