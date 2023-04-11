package fr.real.supervision.appliinfo.job.common;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.JobEvent;
import fr.real.supervision.appliinfo.repository.JobEventRepository;

@Service
@Transactional
public class JobService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

	@Autowired
	private JobEventRepository jobEventRepository;

	public JobEvent save(JobEvent jobEvent) {		
		return jobEventRepository.save(jobEvent);
	}

}
