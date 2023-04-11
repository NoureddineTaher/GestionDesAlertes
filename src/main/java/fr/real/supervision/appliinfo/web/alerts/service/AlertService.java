package fr.real.supervision.appliinfo.web.alerts.service;

import java.util.List;
import java.util.Optional;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.web.alerts.dto.AlertDto;

public interface AlertService {

	List<Alert> getAlerts();

	Alert getAlertById(Long id);

	void deleteAlertById(Long id);

	void save(AlertDto dto) throws Exception;

	void updateAlert(AlertDto alertDto) throws Exception;

	AlertDto getAlertDtoById(Long id);

	void updateActiveAlert(long id, boolean active);

	Optional<Alert>  checkIfAlertNameExist(String name);

}
