package fr.real.supervision.appliinfo.job.sms;

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

import fr.real.supervision.appliinfo.connector.sms.SmsClient;
import fr.real.supervision.appliinfo.connector.sms.SmsException;
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
public class SmsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

	private static final String START_SMS_TEMPLATE = "startSmsTemplate";

	@Autowired
	private AlertEventRepository alertEventRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private HolidayRepository holidayRepository;

	@Autowired
	private SmsClient smsClient;

	@Autowired
	private TemplateEngine smsTemplateEngine;

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
	private void sendSms(AlertEvent alertEvent, String template) throws PlatformException, FunctionalException {

		// Reattacher l'alertEvent pour éviter une lazy loading exception
		alertEvent = alertEventRepository.findById(alertEvent.getId()).orElseThrow(() -> new FunctionalException(
				"Tentative d'envoie de sms pour une événement d'alerte qui n'est plus présent en base"));

		// Construire le message à envoyer
		String message;

		// Si 1er champ du msg sms personnalisé vide: construction avec le template
		// classique
		if (StringUtils.isBlank(alertEvent.getAlert().getStartSms())) {
			Context context = new Context();
			context.setVariable("alertEvent", alertEvent);
			message = smsTemplateEngine.process(template, context);

		} else { // Sinon construction avec le sms personnalisé et remplacement des variables

			// Différenciation des cas de début ou fin d'alerte
			if (template.equals(START_SMS_TEMPLATE)) {
				message = alertEvent.getAlert().getStartSms();
			} else {
				message = alertEvent.getAlert().getEndSms();
			}

			message = message.replace("${alertEvent.alert.name}", alertEvent.getAlert().getName());
			message = message.replace("${alertEvent.alert.alarms}",
					StringUtils.join(alertEvent.getAlarmEvents(), " | "));

		}

		// Trouver tous les contacts à qui envoyer le sms
		List<Contact> contacts = alertEvent.getAlert().getDiffusionGroups().stream()
				.flatMap(diffusionGroup -> diffusionGroup.getContacts().stream()).distinct()
				.collect(Collectors.toList());
		String[] phones = contacts.stream().map(Contact::getPhone).toArray(String[]::new);

		// Envoyer le sms
		try {
			smsClient.send(phones, message);
		} catch (SmsException e) {
			throw new PlatformException(e);
		}

	}

	public void sendSmsForAlertEvent(AlertEvent alertEvent, TemporalAmount sendingLatency) {
		switch (alertEvent.getStatus()) {
		case STARTED, CONFIRMED:
			if (alertEvent.getStartSmsSent() == null
					&& alertEvent.getStart().isBefore(LocalDateTime.now().minus(sendingLatency))
					&& alertEvent.getAlert().getHoursWhenSmsIsAllowedList().stream()
							.anyMatch(workingHour -> ((holidayRepository.existsByDay(LocalDate.now())
									&& workingHour.isSameDay(LocalDateTime.now()))
									|| workingHour.contains(LocalDateTime.now())))) {
				try {
					LOGGER.info("Envoi de sms de début pour l'alerte {}", alertEvent.getAlert().getName());
					sendSms(alertEvent, START_SMS_TEMPLATE);
					// Tracer l'envoi de sms
					alertEvent.setStartSmsSent(LocalDateTime.now());
					alertEventRepository.save(alertEvent);
				} catch (PlatformException | FunctionalException e) {
					LOGGER.error("Le sms de l'alertEvent " + alertEvent.getId() + " n'a pas été envoyé", e);
				}
			}
			break;
		case FINISHED:
			if (alertEvent.getStartSmsSent() != null && alertEvent.getEndSmsSent() == null) {
				try {
					// RG : le sms finished est envoyé quelque soit l'heure
					LOGGER.info("Envoi de sms de Fin pour l'alerte {}", alertEvent.getAlert().getName());
					sendSms(alertEvent, "endSmsTemplate");
					alertEvent.setEndSmsSent(LocalDateTime.now());
					alertEventRepository.save(alertEvent);
				} catch (PlatformException | FunctionalException e) {
					LOGGER.error("Le sms de l'alertEvent " + alertEvent.getId() + " n'a pas été envoyé", e);
				}
			}
			break;

		default:
			break;
		}
	}

}
