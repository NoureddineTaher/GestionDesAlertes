package fr.real.supervision.appliinfo.web.holidays.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Holiday;
import fr.real.supervision.appliinfo.repository.HolidayRepository;
import fr.real.supervision.appliinfo.web.holidays.dto.HolidayDto;
import fr.real.supervision.appliinfo.web.holidays.service.HolidayService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class HolidayServiceImpl implements HolidayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayServiceImpl.class);

	@Autowired
	private HolidayRepository holidayRepository;

	@Override
	public List<Holiday> getHolidays() {
		Iterable<Holiday> findAll = holidayRepository.findAll();
		List<Holiday> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getDay().compareTo(c2.getDay()));
		return collect;
	}

	@Override
	public void save(HolidayDto dto) {
		Holiday holiday = populateHolidayFromHolidayDto(dto);
		holiday.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
		holidayRepository.save(holiday);
		LOGGER.info("Creation du jour ferie : {} par {} le {}  ", holiday.getDay(), holiday.getCreatedBy(),
				holiday.getCreatedDate());
	}

	@Override
	public void updateHoliday(HolidayDto holidayDto) {
		Holiday holiday = getHolidayById(holidayDto.getId());
		populateExistingHolidayFromHolidayDto(holiday, holidayDto);
		holiday.setLastModifiedDate(LocalDateTime.now());
		holiday.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		holidayRepository.save(holiday);
		LOGGER.info("Modification du jour ferie : {} par {} le {}  ", holiday.getDay(), holiday.getLastModifiedBy(),
				holiday.getLastModifiedDate());
	}

	@Override
	public Holiday getHolidayById(Long id) {
		return holidayRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteHolidayById(Long id) {
		holidayRepository.deleteById(id);
	}

	@Override
	public HolidayDto getHolidayDtoById(Long id) {
		Holiday holiday = getHolidayById(id);
		if (holiday == null) {
			return null;
		} else {
			return populateHolidayDtoFromHoliday(holiday);
		}
	}

	private Holiday populateHolidayFromHolidayDto(HolidayDto dto) {
		Holiday holiday = new Holiday();
		return populateExistingHolidayFromHolidayDto(holiday, dto);
	}

	private Holiday populateExistingHolidayFromHolidayDto(Holiday holiday, HolidayDto dto) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dayDate = LocalDate.parse(dto.getDay(), dateTimeFormatter);
		holiday.setDay(dayDate);
		return holiday;
	}

	private HolidayDto populateHolidayDtoFromHoliday(Holiday holiday) {

		HolidayDto holidayDto = new HolidayDto();
		holidayDto.setId(holiday.getId());
		holidayDto.setDay(holiday.getDay().toString());

		return holidayDto;
	}

}
