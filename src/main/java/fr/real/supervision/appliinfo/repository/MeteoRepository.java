package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.Meteo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface MeteoRepository extends PagingAndSortingRepository<Meteo, Long> {

	@Query("select m from Meteo m where m.id = ?1")
	Meteo getMeteoById(Long id);

	@Query("select max (m.id) from Meteo m where m.section = 3")
	Long getLastMeteo();

	@Query("select  m from Meteo m where m.section = ?1 order by m.id")
	List<Meteo> findBySection(Long section);


}
