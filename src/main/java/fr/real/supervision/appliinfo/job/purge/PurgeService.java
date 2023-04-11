package fr.real.supervision.appliinfo.job.purge;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.AlertEventStatus;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import fr.real.supervision.appliinfo.repository.JobEventRepository;

@Service
@Transactional
public class PurgeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PurgeService.class);

	@Autowired
	private AlertEventRepository alertEventRepository;

	@Autowired
	private JobEventRepository jobEventRepository;

	/**
	 * Purge les évènements fini antérieur à un délai
	 * 
	 * @param delay Délai de rétention des événments
	 * 
	 */
	public void purgeEvent(Duration delay) {

		alertEventRepository.deleteByStatusAndLastUpdateBefore(AlertEventStatus.FINISHED,
				LocalDateTime.now().minus(delay));
	}

	/**
	 * Purge les traces antérieures à un delay
	 * 
	 * @param delay Délai de rétention des événments
	 * 
	 */
	public void purgeTrace(Duration delay) {
		jobEventRepository.deleteByEndBefore(LocalDateTime.now().minus(delay));
	}
}
