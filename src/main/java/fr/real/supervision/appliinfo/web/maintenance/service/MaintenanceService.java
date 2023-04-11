package fr.real.supervision.appliinfo.web.maintenance.service;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Maintenance;
import fr.real.supervision.appliinfo.web.maintenance.dto.MaintenanceDto;

import java.util.List;

public interface MaintenanceService {

    List<Maintenance> getOnGoingMaintenancesByAlert(Alert alert);

    List<MaintenanceDto> getSortedByStartTimeMaintenancesDto(Integer numberOfDaysInPastToInclude);

    void saveMultiMaintenances(MaintenanceDto dto);

    void updateMaintenance(MaintenanceDto maintenanceDto);

    Maintenance getMaintenanceById(Long id);

    void deleteMaintenanceById(Long id);

    void deleteMaintenancesByGroup(List<Long> ids);

    MaintenanceDto getMaintenanceDtoById(Long id);

    List<MaintenanceDto> getFutureMaintenancesDtoByAlertId(Long id);
}
