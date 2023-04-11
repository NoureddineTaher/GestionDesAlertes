package fr.real.supervision.appliinfo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.model.AlertEventStatus;

public interface AlertEventRepository extends PagingAndSortingRepository<AlertEvent, Long> {

	/**
	 * Retourne le dernier évènement pour une alerte donnée
	 * 
	 * @param alert
	 * @return
	 */
	@Nullable
	AlertEvent findFirstByAlertOrderByStartDesc(Alert alert);

	void deleteByStatusAndLastUpdateBefore(AlertEventStatus status, LocalDateTime time);

	List<AlertEvent> findByAlertId(Long id);

}
