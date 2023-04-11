package fr.real.supervision.appliinfo.web.alerts.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.model.Maintenance;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import fr.real.supervision.appliinfo.repository.AlertRepository;
import fr.real.supervision.appliinfo.repository.MaintenanceRepository;
import fr.real.supervision.appliinfo.web.alerts.dto.AlertDto;
import fr.real.supervision.appliinfo.web.alerts.service.AlertService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class AlertServiceImpl implements AlertService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private AlertEventRepository alertEventRepository;

	@Autowired
	private MaintenanceRepository maintenanceRepository;

	@Override
	public List<Alert> getAlerts() {
		Iterable<Alert> findAll = alertRepository.findAll();
		List<Alert> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		return collect;
	}

	@Override
	public Alert getAlertById(Long id) {
		return alertRepository.findById(id).orElse(null);
	}

	@Override
	public AlertDto getAlertDtoById(Long id) {
		Alert alert = getAlertById(id);
		if (alert == null) {
			return null;
		} else {
			return populateAlertDtoFromAlert(alert);
		}
	}

	@Override
	public void deleteAlertById(Long id) {
		// Delete all maintenances associated to Alert to remove
		List<Maintenance> maintenances = maintenanceRepository.findByAlertId(id);
		if (!maintenances.isEmpty()) {
			maintenances.forEach(x -> maintenanceRepository.delete(x));
		}
		List<AlertEvent> alertEvents = alertEventRepository.findByAlertId(id);
		if (!alertEvents.isEmpty()) {
			alertEvents.forEach(x -> alertEventRepository.delete(x));
		}
		alertRepository.deleteById(id);
	}

	@Override
	public void save(AlertDto dto) throws Exception {
		Alert alert = populateAlertFromAlertDto(dto);
		try {
			alert.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
			alertRepository.save(alert);
			LOGGER.info("Creation d'une alerte : {} par {} le {} ", alert, alert.getCreatedBy(),
					alert.getCreatedDate());
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Override
	public void updateAlert(AlertDto alertDto) {
		Alert alert = getAlertById(alertDto.getId());
		LOGGER.info("Debut  modification d'une alerte avec une valeur initiale : {}", alert);
		// Keep id, active status and maintenances from alert bean - Set other fields
		// from from dto
		populateExistingAlertFromAlertDto(alert, alertDto);
		alert.setLastModifiedDate(LocalDateTime.now());
		alert.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		alertRepository.save(alert);
		LOGGER.info("Modification : {} par {} le {} avec succes", alert, alert.getLastModifiedBy(),
				alert.getLastModifiedDate());
	}

	@Override
	public void updateActiveAlert(long id, boolean active) {
		Optional<Alert> alert = alertRepository.findById(id);
		if (alert.isPresent()) {
			Alert a = alert.get();
			a.setActive(active);
			alertRepository.save(a);
		}
	}

	private Alert populateAlertFromAlertDto(AlertDto dto) {
		Alert alert = new Alert();
		alert.setActive(true);
		return populateExistingAlertFromAlertDto(alert, dto);
	}

	// Used for alert edition: id, active status and maintenances are kept from
	// model object (edition is not enabled here)
	private Alert populateExistingAlertFromAlertDto(Alert alert, AlertDto dto) {
		alert.setName(dto.getName().toUpperCase());
		alert.setDescription(dto.getDescription());
		alert.setAlarms(dto.getAlarms());
		alert.setCategory(dto.getCategory());
		alert.setDiffusionGroups(new HashSet<>(dto.getDiffusionGroups()));
		alert.setHoursWhenMailIsAllowed(dto.hoursWhenMailIsAllowedString());
		alert.setHoursWhenSmsIsAllowed(dto.hoursWhenSmsIsAllowedString());
		alert.setHoursWhenAstreinteIsAllowed(dto.hoursWhenAstreinteIsAllowedString());

		alert.setStartSms(dto.getStartSms());
		alert.setEndSms(dto.getEndSms());
		alert.setStartSubject(dto.getStartSubject());
		alert.setStartBody(dto.getStartBody());
		alert.setEndSubject(dto.getEndSubject());
		alert.setEndBody(dto.getEndBody());
		alert.setAstreinte(dto.getAstreinte());

		return alert;
	}

	private AlertDto populateAlertDtoFromAlert(Alert alert) {
		AlertDto alertDto = new AlertDto();
		alertDto.setId(alert.getId());
		alertDto.setName(alert.getName());
		alertDto.setActive(alert.isActive());
		alertDto.setDescription(alert.getDescription());
		alertDto.setAlarms(alert.getAlarms());
		alertDto.setCategory(alert.getCategory());
		alertDto.setDiffusionGroups(new ArrayList<>(alert.getDiffusionGroups()));
		if (alert.getHoursWhenMailIsAllowed() != null) {
			alertDto.setHoursWhenMailIsAllowed(alert.getHoursWhenMailIsAllowed());
		}
		if (alert.getHoursWhenSmsIsAllowedList() != null) {
			alertDto.setHoursWhenSmsIsAllowed(alert.getHoursWhenSmsIsAllowedList());
		}
		if (alert.getHoursWhenAstreinteIsAllowedList() != null) {
			alertDto.setHoursWhenAstreinteIsAllowed(alert.getHoursWhenAstreinteIsAllowedList());
		}

		alertDto.setEndSms(alert.getEndSms());
		alertDto.setStartSms(alert.getStartSms());
		alertDto.setStartSubject(alert.getStartSubject());
		alertDto.setStartBody(alert.getStartBody());
		alertDto.setEndSubject(alert.getEndSubject());
		alertDto.setEndBody(alert.getEndBody());
		alertDto.setAstreinte(alert.getAstreinte());

		return alertDto;
	}

	// Permet de contrôler l'unicité Nom de l'alerte
	@Override
	public Optional<Alert> checkIfAlertNameExist(String name) {
		return alertRepository.findAlertByNameIgnoreCase(name);

	}

}
