package fr.real.supervision.appliinfo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.Alert;

public interface AlertRepository extends PagingAndSortingRepository<Alert, Long> {

	Alert findByName(String name);

	Iterable<Alert> findByActiveTrue();

	List<Alert> findByCategoryId(Long id);

	List<Alert> findByDiffusionGroupsId(Long id);

	Optional<Alert> findAlertByNameIgnoreCase(String name);

}
