package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.Holiday;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface HolidayRepository extends PagingAndSortingRepository<Holiday, Long> {

    boolean existsByDay(LocalDate day);

}
