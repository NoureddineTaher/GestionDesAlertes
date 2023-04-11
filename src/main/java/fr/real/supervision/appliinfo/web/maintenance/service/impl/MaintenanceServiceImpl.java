package fr.real.supervision.appliinfo.web.maintenance.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Maintenance;
import fr.real.supervision.appliinfo.repository.MaintenanceRepository;
import fr.real.supervision.appliinfo.web.maintenance.dto.MaintenanceDto;
import fr.real.supervision.appliinfo.web.maintenance.service.MaintenanceService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

	@Autowired
	private MaintenanceRepository maintenanceRepository;

	@Override
	public List<Maintenance> getOnGoingMaintenancesByAlert(Alert alert) {

		Iterable<Maintenance> iterableMaintenances = maintenanceRepository.findByAlert(alert);
		List<Maintenance> maintenances = StreamSupport.stream(iterableMaintenances.spliterator(), false)
				.collect(Collectors.toList());

		List<Maintenance> onGoingMaintenancesByAlert = new ArrayList<>();
		LocalDateTime timeNow = LocalDateTime.now();

		for (Maintenance maintenance : maintenances) {

			if (maintenance.getStartTime().isBefore(timeNow) && maintenance.getEndTime().isAfter(timeNow)) {
				onGoingMaintenancesByAlert.add(maintenance);
			}
		}

		return onGoingMaintenancesByAlert;
	}

	@Override
	public List<MaintenanceDto> getSortedByStartTimeMaintenancesDto(Integer numberOfDaysInPastToInclude) {

		Iterable<Maintenance> iterableMaintenances = maintenanceRepository.findAll();
		List<Maintenance> maintenances = StreamSupport.stream(iterableMaintenances.spliterator(), false)
				.collect(Collectors.toList());

		maintenances.sort((maintenance1, maintenance2) -> {
			return maintenance1.getStartTime().compareTo(maintenance2.getStartTime());
		});
		maintenances.sort((maintenance1, maintenance2) -> {
			return maintenance1.getAlert().getName().compareTo(maintenance2.getAlert().getName());
		});

		List<MaintenanceDto> maintenancesDto = new ArrayList<>();

		for (Maintenance maintenance : maintenances) {
			// Keep only ongoing maintenances, and terminated maintenances since a max
			// number of days
			if (maintenance.getEndTime().isAfter(LocalDateTime.now().minusDays(numberOfDaysInPastToInclude))) {
				maintenancesDto.add(populateMaintenanceDtoFromMaintenanceWithFormattedDate(maintenance));
			}
		}

		return maintenancesDto;
	}

	@Override
	public void saveMultiMaintenances(MaintenanceDto maintenanceDto) {

		// Save one Maintenance bean with same time slot for each selected Alert
		for (Alert alert : maintenanceDto.getAlerts()) {
			Maintenance maintenance = new Maintenance();
			populateExistingMaintenanceFromMaintenanceDto(maintenance, maintenanceDto);
			maintenance.setAlert(alert);
			maintenance.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
			maintenanceRepository.save(maintenance);
			LOGGER.info("Creation d'une maintenance d'alerte : {} par {} le {}", maintenance.getAlert(),
					maintenance.getCreatedBy(), maintenance.getCreatedDate());
		}
	}

	@Override
	public void updateMaintenance(MaintenanceDto maintenanceDto) {
		Maintenance maintenance = getMaintenanceById(maintenanceDto.getId());
		LOGGER.info("Debut de la mise a jour , valeur initiale : {}  ", maintenance);
		// Keep id and alert from maintenance bean - set start/end date and comments
		// from dto
		populateExistingMaintenanceFromMaintenanceDto(maintenance, maintenanceDto);
		maintenance.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		maintenance.setLastModifiedDate(LocalDateTime.now());
		maintenanceRepository.save(maintenance);
		LOGGER.info("Mise à jour de la maintenance d'alerte avec succes :  {} par {} le {}", maintenance,
				maintenance.getLastModifiedBy(), maintenance.getLastModifiedDate());
	}

	@Override
	public Maintenance getMaintenanceById(Long id) {
		return maintenanceRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteMaintenanceById(Long id) {
		maintenanceRepository.deleteById(id);
	}

	@Override
	public void deleteMaintenancesByGroup(List<Long> ids) {
		maintenanceRepository.deleteAllById(ids);
	}

	@Override
	public MaintenanceDto getMaintenanceDtoById(Long id) {
		Maintenance maintenance = getMaintenanceById(id);
		if (maintenance == null) {
			return null;
		} else {
			return populateMaintenanceDtoFromMaintenance(maintenance);
		}
	}

	@Override
	public List<MaintenanceDto> getFutureMaintenancesDtoByAlertId(Long id) {
		List<Maintenance> maintenances = maintenanceRepository.findByAlertId(id);
		List<MaintenanceDto> maintenancesDto = new ArrayList<>();

		for (Maintenance maintenance : maintenances) {
			if (maintenance.getEndTime().isAfter(LocalDateTime.now())) {
				maintenancesDto.add(populateMaintenanceDtoFromMaintenanceWithFormattedDate(maintenance));
			}
		}
		return maintenancesDto;
	}

	// Used for maintenances creation and update
	private void populateExistingMaintenanceFromMaintenanceDto(Maintenance maintenance, MaintenanceDto dto) {

		maintenance.setComments(dto.getComments());

		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			LocalDateTime startLocalDateTime = LocalDateTime.parse(dto.getStartTime(), dateTimeFormatter);
			maintenance.setStartTime(startLocalDateTime);
			LocalDateTime endLocalDateTime = LocalDateTime.parse(dto.getEndTime(), dateTimeFormatter);
			maintenance.setEndTime(endLocalDateTime);

		} catch (Exception e) {
			LOGGER.error(
					"PROBLEM DURING START/ENDTIME CONVERSION in MaintenanceServiceImpl.populateMaintenanceFromMaintenanceDto()",
					e);
		}
	}

	// Used for list of maintenances display
	private MaintenanceDto populateMaintenanceDtoFromMaintenanceWithFormattedDate(Maintenance maintenance) {

		MaintenanceDto maintenanceDto = new MaintenanceDto();
		maintenanceDto.setId(maintenance.getId());
		maintenanceDto.setAlert(maintenance.getAlert());
		maintenanceDto.setComments(maintenance.getComments());
		maintenanceDto.setLastModifiedBy(maintenance.getLastModifiedBy());

		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			maintenanceDto.setStartTime(maintenance.getStartTime().format(dateTimeFormatter));
			maintenanceDto.setEndTime(maintenance.getEndTime().format(dateTimeFormatter));
			if (null != maintenance.getLastModifiedDate()) {
				maintenanceDto.setLastModifiedDate(maintenance.getLastModifiedDate().format(dateTimeFormatter));
			}
		} catch (Exception e) {
			LOGGER.error(
					"PROBLEM DURING START/ENDTIME/LASTMODIFIEDDATE CONVERSION in MaintenanceServiceImpl.populateMaintenanceDtoFromMaintenance()",
					e);
		}
		return maintenanceDto;
	}

	// Used for maintenance update
	private MaintenanceDto populateMaintenanceDtoFromMaintenance(Maintenance maintenance) {

		MaintenanceDto maintenanceDto = new MaintenanceDto();
		maintenanceDto.setId(maintenance.getId());
		maintenanceDto.setStartTime(maintenance.getStartTime().toString());
		maintenanceDto.setEndTime(maintenance.getEndTime().toString());
		maintenanceDto.setComments(maintenance.getComments());
		maintenanceDto.setAlert(maintenance.getAlert());
		maintenanceDto.setLastModifiedBy(maintenance.getLastModifiedBy());
		if (null != maintenance.getLastModifiedDate()) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			maintenanceDto.setLastModifiedDate(maintenance.getLastModifiedDate().format(dateTimeFormatter));
		}

		return maintenanceDto;
	}

}
