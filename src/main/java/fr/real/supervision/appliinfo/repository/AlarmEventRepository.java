package fr.real.supervision.appliinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.AlarmEvent;

public interface AlarmEventRepository extends PagingAndSortingRepository<AlarmEvent, Long>{

	
}
