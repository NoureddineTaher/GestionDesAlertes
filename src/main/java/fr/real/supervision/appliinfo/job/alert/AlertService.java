package fr.real.supervision.appliinfo.job.alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.real.supervision.appliinfo.connector.itm.ItmAlarm;
import fr.real.supervision.appliinfo.exception.PlatformException;
import fr.real.supervision.appliinfo.model.Alarm;
import fr.real.supervision.appliinfo.model.AlarmEvent;
import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.model.AlertEventStatus;
import fr.real.supervision.appliinfo.repository.AlarmRepository;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import fr.real.supervision.appliinfo.repository.AlertRepository;
import fr.real.supervision.appliinfo.repository.JobEventRepository;

@Service
@Transactional
public class AlertService {

	private static final int ALERT_SEUIL = 100;

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

	/**
	 * Formatter pour les timestamp retournés par ITM
	 */
	private static final DateTimeFormatter ITM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("1yyMMddHHmmssSSS");

	@Autowired
	private AlarmRepository alarmRepository;

	@Autowired
	private AlertEventRepository alertEventRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private JobEventRepository jobEventRepository;

	public void consolidateAlertEvents(List<ItmAlarm> itmAlarms) throws PlatformException {
		LOGGER.debug("Consolidation des alertes...");
		// Filtrer les alarmes pour ne retenir que celles considerees comme des
		// évènements pour appliinfo
		List<AlarmEvent> alarmEvents = mapItmAlarms(itmAlarms);

		// Calculer les nouvelles alertes en fonction des alarmes itm retenues
		List<AlertEvent> newAlertEvents = consolidateAlert(alarmEvents);

		// Pour toutes les alertes surveillées active
		Iterable<Alert> alerts = alertRepository.findByActiveTrue();
		Iterator<Alert> it = alerts.iterator();
		while (it.hasNext()) {
			Alert alert = it.next();

			// Vérifie si l'alerte est en maintenance
			if (!alert.isActive()) {
				LOGGER.error("Alerte passee pour cause de maintenance : {}", alert.getName());
				continue;
			}

			// Réccupérer le dernier évènement en cours en bdd
			AlertEvent event = alertEventRepository.findFirstByAlertOrderByStartDesc(alert);

			// Réccupérer l'événement correspondant dans les nouveaux événements calculés
			Optional<AlertEvent> newEvent = newAlertEvents.stream().filter(e -> alert.equals(e.getAlert())).findAny();

			if (event == null || AlertEventStatus.FINISHED.equals(event.getStatus())) {
				newEvent.ifPresent(e -> {
					// Créer un nouveu événement d'alerte
					LOGGER.info("Creation d'une nouvelle alerte {} avec les alarmes suivantes {}",
							newEvent.get().getAlert().getName(), newEvent.get().getAlarmEvents().stream()
									.map(AlarmEvent::toString).collect(Collectors.joining(";", "[", "]")));
					alertEventRepository.save(newEvent.get());
				});
			} else {
				if (newEvent.isPresent()) {
					// Mettre à jour l'alertEvent
					LOGGER.info("Confirmation de l'alerte {} (ID = {}) avec les alarmes suivantes {}",
							event.getAlert().getName(), event.getAlert().getId(), newEvent.get().getAlarmEvents()
									.stream().map(AlarmEvent::toString).collect(Collectors.joining(";", "[", "]")));
					event.setStatus(AlertEventStatus.CONFIRMED);
					event.setLastUpdate(LocalDateTime.now());
					event.setWeightSum(newEvent.get().getWeightSum());
					event.getAlarmEvents().clear();
					event.getAlarmEvents().addAll(newEvent.get().getAlarmEvents());
					alertEventRepository.save(event);
				} else {
					// Mettre fin à l'alertEvent
					LOGGER.info("Fin de l'alerte {}", event.getAlert().getName());
					event.setStatus(AlertEventStatus.FINISHED);
					event.setLastUpdate(LocalDateTime.now());
					alertEventRepository.save(event);
				}
			}
		}
	}

	/**
	 * Fait correspondre une alarme itm aux alarmes correspondantes dans appliInfo
	 * pour produire une liste d'événement d'alarme
	 *
	 * @param itmAlarm
	 *
	 * @return
	 * @throws PlatformException
	 */
	// TODO Ce mapping devrait peut etre se faire directement dans le client ITM
	// pour ne pas manipuler d'objet ITM alarm en dehors du package connector
	private List<AlarmEvent> mapItmAlarm(ItmAlarm itmAlarm) throws PlatformException {

		List<AlarmEvent> alarmEvents = new ArrayList<>();

		// Vérifier si l'alarme itm fait partie des alarmes connues d'appliinfo
		List<Alarm> potentialAlarms = alarmRepository.findByName(itmAlarm.getName());

		for (Alarm potentialAlarm : potentialAlarms) {

			// Verifier que le nom du serveur correspond au pattern enregistré pour cette
			// alarm
			boolean isServerNameMatching = false;
			String serverNamePattern = potentialAlarm.getServer();
			if (StringUtils.hasLength(serverNamePattern)) {
				try {
					Pattern p = Pattern.compile(serverNamePattern);
					Matcher m = p.matcher(itmAlarm.getServer());
					if (m.find()) {
						// Le serveur de l'alarme trouvée correspond bien au pattern attenedu
						isServerNameMatching = true;
					}
				} catch (Exception e) {
					// en cas d'erreur de pattern matching ou autre
					throw new PlatformException("Erreur lors du pattern matching sur le nom de serveur de l'alarme "
							+ potentialAlarm.getName(), e);
				}
			} else {
				// si le pattern n'est pas renseigné ou qu'il est vide, ça matche
				// systématiquement
				isServerNameMatching = true;
			}

			if (!isServerNameMatching) {
				LOGGER.warn("L'alarme {} suivante n'a pas ete retenue par la regexp {}", itmAlarm,
						potentialAlarm.getServer());
			}

			// Si l'alarme correspond bien à une alarme suivie par appliinfo, alors ajouter
			// un événement d'alarm
			if (isServerNameMatching) {
				AlarmEvent alarmEvent = new AlarmEvent();
				alarmEvent.setAlarm(potentialAlarm);
				alarmEvent.setStart(LocalDateTime.parse(itmAlarm.getStart(), ITM_DATE_TIME_FORMATTER));

				alarmEvents.add(alarmEvent);
			}
		}

		return alarmEvents;
	}

	/**
	 * Associe une liste d'alarme itm aux alarmes correspondantes dans appliInfo
	 * pour produire une liste d'événement d'alarme
	 *
	 * @param itmAlarms
	 *
	 * @return
	 * @throws PlatformException
	 */
	private List<AlarmEvent> mapItmAlarms(List<ItmAlarm> itmAlarms) throws PlatformException {
		List<AlarmEvent> alarmEvents = new ArrayList<>();
		for (ItmAlarm itmAlarm : itmAlarms) {
			alarmEvents.addAll(mapItmAlarm(itmAlarm));
		}
		return alarmEvents;
	}

	/**
	 * Etant donné une liste d'évènement d'alarme, calcule les évènement d'alertes
	 * correspondants
	 *
	 * @param alarmEvents
	 */
	private List<AlertEvent> consolidateAlert(List<AlarmEvent> alarmEvents) {

		// Associer les événements d'alarme à une alerte active
		Map<Alert, List<AlarmEvent>> listAlarmsByAlerts = new HashMap<>();
		for (AlarmEvent alarmEvent : alarmEvents) {
			// On n eboucle que sur les alertes actives

			for (Alert alert : alarmEvent.getAlarm().getAlerts().stream().filter(Alert::isActive)
					.collect(Collectors.toList())) {
				if (listAlarmsByAlerts.get(alert) == null) {
					List<AlarmEvent> events = new ArrayList<>();
					events.add(new AlarmEvent(alarmEvent));
					listAlarmsByAlerts.put(alert, events);

				//Sinon encore vérifier si la source de l'AlarmEvent existe déja dans la liste des sources de ceux associées à l'alerte
				} else /**if(!listAlarmsByAlerts.get(alert).stream().map(e->e.getAlarm().getId()).collect(Collectors.toList()).contains(alarmEvent.getAlarm().getId()))**/ {
					listAlarmsByAlerts.get(alert).add(new AlarmEvent(alarmEvent));
				}
			}
		}

		// calculer les événements d'alerte
		List<AlertEvent> alertEvents = new ArrayList<>();
		for (Map.Entry<Alert, List<AlarmEvent>> entry : listAlarmsByAlerts.entrySet()) {
			Alert alert = entry.getKey();
			List<AlarmEvent> events = entry.getValue();

			Integer weightSum = events.stream().map(x -> x.getAlarm().getWeight()).reduce(0, Integer::sum);
			// Ne retenir que les évènements d'alerte >= ALERT_SEUIL
			if (weightSum >= ALERT_SEUIL) {
				AlertEvent alertEvent = new AlertEvent();
				alertEvent.setAlert(alert);
				// Par défaut l'événement est considéré STARTED
				alertEvent.setStatus(AlertEventStatus.STARTED);
				LocalDateTime now = LocalDateTime.now();
				alertEvent.setStart(now);
				alertEvent.setLastUpdate(now);
				alertEvent.setAlarmEvents(events);
				// calcul du cout
				alertEvent.setWeightSum(weightSum);
				alertEvents.add(alertEvent);
			}
		}

		return alertEvents;
	}
}