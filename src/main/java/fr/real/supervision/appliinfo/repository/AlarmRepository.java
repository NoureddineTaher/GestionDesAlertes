package fr.real.supervision.appliinfo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.Alarm;

public interface AlarmRepository extends PagingAndSortingRepository<Alarm, Long>{

	List<Alarm> findByName(String name);
	
}
