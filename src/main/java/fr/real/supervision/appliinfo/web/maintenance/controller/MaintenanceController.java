package fr.real.supervision.appliinfo.web.maintenance.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Maintenance;
import fr.real.supervision.appliinfo.web.alerts.service.AlertService;
import fr.real.supervision.appliinfo.web.maintenance.dto.MaintenanceDto;
import fr.real.supervision.appliinfo.web.maintenance.service.MaintenanceService;

@Controller
public class MaintenanceController {

	private static final String VIEW_MAINTENANCES = "maintenances/maintenances";

	private static final String ATTRIBUTE_LISTALERTS = "listAlerts";

	private static final String VIEW_ADD_MAINTENANCE = "maintenances/add_maintenance";

	private static final String ATTRIBUTE_MAINTENANCE_DTO = "maintenanceDto";

	private static final String FIELD_STARTTIME = "startTime";

	private static final String STARTTIME_ERROR_MSG = "Attention, la date de début doit précéder la date de fin";

	private static final String REDIRECT_MAINTENANCES = "redirect:/maintenances";

	private static final String ATTRIBUTE_MAINTENANCES = "maintenances";

	private static final String VIEW_UPDATE_MAINTENANCE = "maintenances/update_maintenance";

	private static final String VERSION = "version";

	@Autowired
	private MaintenanceService maintenanceService;

	@Autowired
	private AlertService alertService;

	@Value("${maintenance.archive.numberOfDaysInPast}")
	private String numberOfDaysInPast;

	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("/maintenances")
	public ModelAndView getMaintenances(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_MAINTENANCES);

		List<MaintenanceDto> maintenancesDto = maintenanceService.getSortedByStartTimeMaintenancesDto(0);

		modelAndView.addObject(ATTRIBUTE_MAINTENANCES, maintenancesDto);
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@GetMapping("/maintenances/archived")
	public ModelAndView getArchivedMaintenances(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_MAINTENANCES);

		// Default Value for archived maintenances: 30 days
		Integer numberOfDays = 30;
		if (isNumeric(numberOfDaysInPast)) {
			numberOfDays = Integer.valueOf(numberOfDaysInPast);
		}

		List<MaintenanceDto> maintenancesDto = maintenanceService.getSortedByStartTimeMaintenancesDto(numberOfDays);

		modelAndView.addObject(ATTRIBUTE_MAINTENANCES, maintenancesDto);
		modelAndView.addObject(VERSION, applicationVersion);

		return modelAndView;
	}

	@GetMapping("/maintenances/addmaintenance")
	public ModelAndView addMaintenance(Model model) {

		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_MAINTENANCE);
		MaintenanceDto maintenanceDto = new MaintenanceDto();
		modelAndView.addObject(ATTRIBUTE_LISTALERTS, alertService.getAlerts());
		modelAndView.addObject(ATTRIBUTE_MAINTENANCE_DTO, maintenanceDto);
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;

	}

	@PostMapping("/maintenances/addmaintenance")
	public ModelAndView addMaintenance(
			@Valid @ModelAttribute(value = ATTRIBUTE_MAINTENANCE_DTO) MaintenanceDto maintenanceDto,
			BindingResult bindingResult, Model model) {

		if (!maintenanceDto.validate()) {
			bindingResult.addError(new FieldError(ATTRIBUTE_MAINTENANCE_DTO, FIELD_STARTTIME, STARTTIME_ERROR_MSG));
		}

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_ADD_MAINTENANCE);
			modelAndView.addObject(ATTRIBUTE_LISTALERTS, alertService.getAlerts());
			modelAndView.addObject(ATTRIBUTE_MAINTENANCE_DTO, maintenanceDto);
			return modelAndView;
		}
		maintenanceService.saveMultiMaintenances(maintenanceDto);
		return new ModelAndView(REDIRECT_MAINTENANCES);
	}

	@GetMapping("/maintenances/update/{id}")
	public ModelAndView updateMaintenance(@PathVariable("id") long id, Model model,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		MaintenanceDto maintenanceDto = maintenanceService.getMaintenanceDtoById(id);

		if (maintenanceDto != null) {
			modelAndView.setViewName(VIEW_UPDATE_MAINTENANCE);
			modelAndView.addObject(ATTRIBUTE_MAINTENANCE_DTO, maintenanceDto);

		} else {
			modelAndView.setViewName(REDIRECT_MAINTENANCES);
			populateReturnError(id, redirectAttributes);
		}
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@PostMapping("/maintenances/update/{id}")
	public ModelAndView updateMaintenance(@PathVariable("id") long id,
			@Valid @ModelAttribute(value = ATTRIBUTE_MAINTENANCE_DTO) MaintenanceDto maintenanceDto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		Maintenance maintenance = maintenanceService.getMaintenanceById(id);
		if (maintenance == null) {
			populateReturnError(id, redirectAttributes);
			return new ModelAndView(REDIRECT_MAINTENANCES);
		}

		if (!maintenanceDto.validate()) {
			bindingResult.addError(new FieldError(ATTRIBUTE_MAINTENANCE_DTO, FIELD_STARTTIME, STARTTIME_ERROR_MSG));
		}

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_UPDATE_MAINTENANCE);
			// Set Alert to display alert name
			maintenanceDto.setAlert(maintenance.getAlert());
			modelAndView.addObject(ATTRIBUTE_MAINTENANCE_DTO, maintenanceDto);
			return modelAndView;
		}

		maintenanceService.updateMaintenance(maintenanceDto);
		return new ModelAndView(REDIRECT_MAINTENANCES);

	}

	@GetMapping("/maintenances/deleteByGroup/{ids}")
	public ModelAndView deleteMaintenancesByGroup(@PathVariable("ids") String ids) {

		List<String> idslist= Arrays.stream(ids.split("-")).toList();
		List<Long> idsToDelete = idslist.stream().map(id -> (long) Integer.parseInt(id)).collect(Collectors.toList());
		maintenanceService.deleteMaintenancesByGroup(idsToDelete);
		return new ModelAndView(REDIRECT_MAINTENANCES);

	}
	@GetMapping("/maintenances/delete/{id}")
	public ModelAndView deleteMaintenance(@PathVariable("id") long id, Model model,
			RedirectAttributes redirectAttribute) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_MAINTENANCES);
		Maintenance maintenance = maintenanceService.getMaintenanceById(id);

		if (maintenance != null) {
			maintenanceService.deleteMaintenanceById(id);
		} else {
			populateReturnError(id, redirectAttribute);
		}
		return modelAndView;

	}

	private void populateReturnError(Long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Id Maintenance invalide : " + id);
	}

	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}


}
