package fr.real.supervision.appliinfo.web.incidents.repository;

import fr.real.supervision.appliinfo.web.incidents.models.Incident;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IncidentRepository extends PagingAndSortingRepository<Incident, Long> {


    // selection d'incident le plus recent
    public Incident findFirstByOrderByIdDesc();

}