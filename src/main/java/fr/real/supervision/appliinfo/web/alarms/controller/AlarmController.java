package fr.real.supervision.appliinfo.web.alarms.controller;

import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.web.alarms.dto.AlarmDto;
import fr.real.supervision.appliinfo.web.alarms.service.AlarmService;

@Controller
public class AlarmController {

	private static final String VIEW_ALARMS = "alarms/alarms";
	private static final String VIEW_ADDALARM = "alarms/add_alarm";
	private static final String VIEW_UPDATEALARM = "alarms/update_alarm";
	private static final String REDIRECT_ALARMS = "redirect:/alarms";
	private static final String ATTRIBUTE_ALARM_DTO = "alarmDto";
	private static final String FIELD_WEIGHT = "weight";
	private static final String ERROR_WEIGHT_MESSAGE = "Attention, le poids doit être un nombre entier positif";
	private static final String VERSION = "version";

	@Autowired
	private AlarmService alarmService;

	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("/alarms")
	public ModelAndView getAlarms(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_ALARMS);
		modelAndView.addObject("alarms", alarmService.getAlarms());
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView ;
	}

	@GetMapping("/alarms/addalarm")
	public ModelAndView addAlarm(AlarmDto alarmDto) {
		return new ModelAndView(VIEW_ADDALARM).addObject(VERSION,applicationVersion);
	}

	@PostMapping("/alarms/addalarm")
	public ModelAndView addAlarm(@Valid AlarmDto alarmDto, BindingResult result, Model model) {
		if(!alarmDto.validateWeight()){
			result.addError(new FieldError(ATTRIBUTE_ALARM_DTO, FIELD_WEIGHT, ERROR_WEIGHT_MESSAGE));
		}
		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName(VIEW_ADDALARM);
			modelAndView.addObject(ATTRIBUTE_ALARM_DTO, alarmDto);
			return modelAndView;
		}
		alarmService.saveAlarm(alarmDto);
		return new ModelAndView(REDIRECT_ALARMS);
	}

	@GetMapping("/alarms/edit/{id}")
	public ModelAndView updateAlarm(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView();
		AlarmDto alarmDto = alarmService.getAlarmDtoById(id);
		if (alarmDto != null) {
			modelAndView.setViewName(VIEW_UPDATEALARM);
			model.addAttribute(ATTRIBUTE_ALARM_DTO, alarmDto);
		} else {
			modelAndView.setViewName(REDIRECT_ALARMS);
			populateReturnError(id, redir);
		}
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView;
	}

	@PostMapping("/alarms/edit/{id}")
	public ModelAndView updateAlarm(@PathVariable("id") long id, @Valid AlarmDto alarmDto, BindingResult result,
			Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_ALARMS);
		if (alarmService.getAlarmDtoById(id) == null) {
			populateReturnError(id, redir);
			return modelAndView;
		}
		if(!alarmDto.validateWeight()){
			result.addError(new FieldError(ATTRIBUTE_ALARM_DTO, FIELD_WEIGHT, ERROR_WEIGHT_MESSAGE));
		}
		if (result.hasErrors()) {
			modelAndView.setViewName(VIEW_UPDATEALARM);
			modelAndView.addObject(ATTRIBUTE_ALARM_DTO, alarmDto);
			return modelAndView;
		}
		alarmService.updateAlarm(alarmDto);
		return modelAndView;
	}

	@GetMapping("/alarms/delete/{id}")
	public ModelAndView deleteAlarm(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_ALARMS);
		AlarmDto alarmDto = alarmService.getAlarmDtoById(id);
		if (alarmDto != null) {
			Set<Alert> alerts = alarmDto.getAlerts();
			if (alerts.isEmpty()) {
				alarmService.deleteAlarmById(id);
			} else {
				populateReturnError(alarmDto, redir, alerts);
			}
		} else {
			populateReturnError(id, redir);
		}
		return modelAndView;
	}

	private void populateReturnError(long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Invalid Alarm Id : " + id);
	}

	private void populateReturnError(AlarmDto alarmDto, RedirectAttributes redir, Set<Alert> alerts) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Impossible de supprimer l'alarme " + alarmDto.getName()
				+ ". Elle est utilisée dans l'alerte(s) suivante(s) : " + StringUtils.join(alerts, ", ") + ".");
	}

}
