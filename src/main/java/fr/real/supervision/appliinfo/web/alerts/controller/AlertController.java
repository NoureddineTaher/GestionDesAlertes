package fr.real.supervision.appliinfo.web.alerts.controller;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.WorkingHour;
import fr.real.supervision.appliinfo.web.alarms.service.AlarmService;
import fr.real.supervision.appliinfo.web.alerts.dto.AlertDto;
import fr.real.supervision.appliinfo.web.alerts.service.AlertService;
import fr.real.supervision.appliinfo.web.category.service.CategoryService;
import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;
import fr.real.supervision.appliinfo.web.maintenance.service.MaintenanceService;

@Controller
public class AlertController {

	private static final String REDIRECT_ALERTS = "redirect:/alerts";

	private static final String ATTRIBUTE_LIST_DIFFUSIONGROUPS = "listDiffusionGroups";

	private static final String ATTRIBUTE_LISTCATEGORIES = "listCategories";

	private static final String ATTRIBUTE_LISTOFDAYS = "listOfDays";

	private static final String ATTRIBUTE_ALERTDTO = "alertdto";

	private static final String ATTRIBUTE_ALERTS = "alerts";

	private static final String ATTRIBUTE_LISTALARMS = "listAlarms";

	private static final String ATTRIBUTE_LISTMAINTENANCES = "listMaintenances";

	private static final String VIEW_ALERTS = "alerts/alerts";

	private static final String VIEW_ADD_ALERT = "alerts/add_alert";

	private static final String VIEW_UPDATE_ALERT = "alerts/update_alert";

	private static final String FIELD_NAME = "name";

	private static final String ERROR_DURING_ALERT_SAVING_MSG = "Erreur lors de l'enregistrement - Champ incorrect (le nom doit être unique)";

	private static final String VERSION = "version";
	@Autowired
	private AlertService alertService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private DiffusionGroupService diffusionGroupService;

	@Autowired
	private MaintenanceService maintenanceService;

	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("/alerts")
	public ModelAndView getAlerts(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_ALERTS);
		modelAndView.addObject(ATTRIBUTE_ALERTS, alertService.getAlerts());
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@GetMapping("/alerts/addalert")
	public ModelAndView addAlert() {

		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_ALERT);
		modelAndView.addObject(VERSION, applicationVersion);
		AlertDto alertDto = new AlertDto();
		alertDto.setActive(true);
		fillAlertModelAndView(modelAndView, alertDto);

		return modelAndView;
	}

	@PostMapping("/alerts/addalert")
	public ModelAndView addAlert(@Valid @ModelAttribute(value = ATTRIBUTE_ALERTDTO) AlertDto alertDto,
			BindingResult result, Model model) throws Exception {

		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_ADD_ALERT);
			fillAlertModelAndViewWithRemovedNullWorkingHours(modelAndView, alertDto);
			return modelAndView;
		}
		if (alertService.checkIfAlertNameExist(alertDto.getName()).isPresent()) {
			result.addError(new FieldError(ATTRIBUTE_ALERTDTO, FIELD_NAME, ERROR_DURING_ALERT_SAVING_MSG));
			ModelAndView modelAndView = new ModelAndView(VIEW_ADD_ALERT);
			fillAlertModelAndViewWithRemovedNullWorkingHours(modelAndView, alertDto);
			return modelAndView;
		}
		alertService.save(alertDto);
		return new ModelAndView(REDIRECT_ALERTS);
	}

	@GetMapping("/alerts/edit/{id}")
	public ModelAndView updateAlert(@PathVariable("id") long id, Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView();
		AlertDto alertDto = alertService.getAlertDtoById(id);

		if (alertDto != null) {
			modelAndView.setViewName(VIEW_UPDATE_ALERT);
			fillAlertModelAndView(modelAndView, alertDto);
			modelAndView.addObject(ATTRIBUTE_LISTMAINTENANCES,
					maintenanceService.getFutureMaintenancesDtoByAlertId(id));
		} else {
			modelAndView.setViewName(REDIRECT_ALERTS);
			populateReturnError(id, redir);
		}
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@PostMapping("/alerts/update/{id}")
	public ModelAndView updateAlert(@PathVariable("id") long id,
			@Valid @ModelAttribute(value = ATTRIBUTE_ALERTDTO) AlertDto alertDto, BindingResult result, Model model,
			RedirectAttributes redir) throws Exception {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_ALERTS);

		if (alertService.getAlertById(id) == null) {
			populateReturnError(id, redir);
			return modelAndView;
		}
		if (result.hasErrors()) {
			modelAndView = new ModelAndView(VIEW_UPDATE_ALERT);
			fillAlertModelAndViewWithRemovedNullWorkingHours(modelAndView, alertDto);
			modelAndView.addObject(ATTRIBUTE_LISTMAINTENANCES,
					maintenanceService.getFutureMaintenancesDtoByAlertId(id));
			return modelAndView;
		}

		try {
			alertService.updateAlert(alertDto);
		} catch (Exception e) {
			result.addError(new FieldError(ATTRIBUTE_ALERTDTO, FIELD_NAME, ERROR_DURING_ALERT_SAVING_MSG));
			modelAndView = new ModelAndView(VIEW_UPDATE_ALERT);
			fillAlertModelAndViewWithRemovedNullWorkingHours(modelAndView, alertDto);
			modelAndView.addObject(ATTRIBUTE_LISTMAINTENANCES,
					maintenanceService.getFutureMaintenancesDtoByAlertId(id));
			return modelAndView;

		}

		return modelAndView;
	}

	@GetMapping("/alerts/delete/{id}")
	public ModelAndView deleteAlert(@PathVariable("id") long id, Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_ALERTS);
		Alert alert = alertService.getAlertById(id);
		if (alert != null) {
			alertService.deleteAlertById(id);
		} else {
			populateReturnError(id, redir);
		}
		return modelAndView;
	}

	@GetMapping("/activationalerts")
	public @ResponseBody String activeAlert(@RequestParam("id") Long id, @RequestParam("active") Boolean active) {
		if (alertService.getAlertById(id) == null) {
			return "Invalide ID";
		}
		alertService.updateActiveAlert(id, active);
		return "OK";
	}

	private void populateReturnError(Long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Invalid Alerte Id : " + id);
	}

	private List<WorkingHour> removeNullWorkingHour(List<WorkingHour> l) {
		if (l != null) {
			return l.stream().filter(s -> s.getDay() != null || s.getMinHour() != null || s.getMaxHour() != null)
					.collect(Collectors.toList());
		} else {
			return l;
		}
	}

	private void fillAlertModelAndView(ModelAndView modelAndView, AlertDto alertDto) {

		modelAndView.addObject(ATTRIBUTE_ALERTDTO, alertDto);
		modelAndView.addObject(ATTRIBUTE_LISTOFDAYS, DayOfWeek.values());
		modelAndView.addObject(ATTRIBUTE_LISTCATEGORIES, categoryService.getCategories());
		modelAndView.addObject(ATTRIBUTE_LIST_DIFFUSIONGROUPS, diffusionGroupService.getDiffusionGroups());
		modelAndView.addObject(ATTRIBUTE_LISTALARMS, alarmService.getAlarms());
	}

	private void fillAlertModelAndViewWithRemovedNullWorkingHours(ModelAndView modelAndView, AlertDto alertDto) {

		alertDto.setHoursWhenAstreinteIsAllowed(removeNullWorkingHour(alertDto.getHoursWhenAstreinteIsAllowed()));
		alertDto.setHoursWhenSmsIsAllowed(removeNullWorkingHour(alertDto.getHoursWhenSmsIsAllowed()));
		alertDto.setHoursWhenMailIsAllowed(removeNullWorkingHour(alertDto.getHoursWhenMailIsAllowed()));

		fillAlertModelAndView(modelAndView, alertDto);
	}

}
