package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Maintenance;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;


public interface MaintenanceRepository extends PagingAndSortingRepository<Maintenance, Long> {

        List<Maintenance> findByAlert(Alert alert);

        List<Maintenance> findByAlertId(Long id);

}
