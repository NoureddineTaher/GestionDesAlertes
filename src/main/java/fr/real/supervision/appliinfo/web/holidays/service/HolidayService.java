package fr.real.supervision.appliinfo.web.holidays.service;

import fr.real.supervision.appliinfo.model.Holiday;
import fr.real.supervision.appliinfo.web.holidays.dto.HolidayDto;

import java.util.List;

public interface HolidayService {

    List<Holiday> getHolidays();

    void save(HolidayDto dto);

    void updateHoliday(HolidayDto holidayDto);

    Holiday getHolidayById(Long id);

    void deleteHolidayById(Long id);

    HolidayDto getHolidayDtoById(Long id);

}
