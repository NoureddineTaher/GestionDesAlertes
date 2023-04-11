package fr.real.supervision.appliinfo.web.alarms.service;

import java.util.List;

import fr.real.supervision.appliinfo.model.Alarm;
import fr.real.supervision.appliinfo.web.alarms.dto.AlarmDto;

public interface AlarmService {

	List<Alarm> getAlarms();

	void saveAlarm(AlarmDto alarmDto);

	void updateAlarm(AlarmDto alarmDto);

	Alarm getAlarmById(Long id);

	AlarmDto getAlarmDtoById(Long id);

	void deleteAlarmById(Long id);

}
