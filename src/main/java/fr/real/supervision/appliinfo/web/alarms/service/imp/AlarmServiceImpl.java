package fr.real.supervision.appliinfo.web.alarms.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Alarm;
import fr.real.supervision.appliinfo.repository.AlarmRepository;
import fr.real.supervision.appliinfo.web.alarms.dto.AlarmDto;
import fr.real.supervision.appliinfo.web.alarms.service.AlarmService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class AlarmServiceImpl implements AlarmService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmRepository alarmRepository;

	@Override
	public List<Alarm> getAlarms() {
		Iterable<Alarm> findAll = alarmRepository.findAll();
		List<Alarm> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		return collect;
	}

	@Override
	public void saveAlarm(AlarmDto alarmDto) {
		Alarm alarm = populateAlarmFromAlarmDto(alarmDto);
		alarm.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
		alarmRepository.save(alarm);
		LOGGER.info("Creation d'une alarme : {} par {} le {}", alarm, alarm.getCreatedBy(), alarm.getCreatedDate());
	}

	@Override
	public void updateAlarm(AlarmDto alarmDto) {
		Alarm ancienneAlarm = getAlarmById(alarmDto.getId());
		Alarm alarm = populateAlarmFromAlarmDto(alarmDto);
		alarm.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		alarm.setLastModifiedDate(LocalDateTime.now());
		LOGGER.info("Modification : {} to {} par {} le {}", ancienneAlarm, alarm, alarm.getLastModifiedBy(),
				alarm.getLastModifiedDate());
		alarmRepository.save(alarm);
		LOGGER.debug("Modification d'une alarme avec succes");
	}

	@Override
	public Alarm getAlarmById(Long id) {
		return alarmRepository.findById(id).orElse(null);
	}

	@Override
	public AlarmDto getAlarmDtoById(Long id) {
		Alarm alarm = getAlarmById(id);
		if (alarm != null) {
			return populateAlarmDtoFromAlarm(alarm);
		} else {
			return null;
		}
	}

	@Override
	public void deleteAlarmById(Long id) {
		alarmRepository.deleteById(id);
	}

	private AlarmDto populateAlarmDtoFromAlarm(Alarm alarm) {
		AlarmDto alarmDto = new AlarmDto();
		alarmDto.setId(alarm.getId());
		alarmDto.setName(alarm.getName());
		alarmDto.setDescription(alarm.getDescription());
		alarmDto.setServer(alarm.getServer());
		alarmDto.setWeight(alarm.getWeight().toString());
		alarmDto.setAlerts(alarm.getAlerts());
		return alarmDto;
	}

	private Alarm populateAlarmFromAlarmDto(AlarmDto alarmDto) {
		Alarm alarm = new Alarm();
		if (alarmDto.getId() != null) {
			alarm.setId(alarmDto.getId());
		}
		alarm.setName(alarmDto.getName());
		alarm.setDescription(alarmDto.getDescription());
		alarm.setServer(alarmDto.getServer());
		alarm.setWeight(Integer.valueOf(alarmDto.getWeight()));
		return alarm;
	}

}
