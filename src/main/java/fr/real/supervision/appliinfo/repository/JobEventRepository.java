package fr.real.supervision.appliinfo.repository;

import java.time.LocalDateTime;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.JobEvent;

public interface JobEventRepository extends PagingAndSortingRepository<JobEvent, Long>{

	void deleteByEndBefore(LocalDateTime time);
	
}
