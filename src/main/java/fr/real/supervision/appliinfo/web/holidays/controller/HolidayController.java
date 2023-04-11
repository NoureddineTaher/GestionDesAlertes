package fr.real.supervision.appliinfo.web.holidays.controller;

import fr.real.supervision.appliinfo.model.Holiday;
import fr.real.supervision.appliinfo.web.holidays.dto.HolidayDto;
import fr.real.supervision.appliinfo.web.holidays.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

    private static final String REDIRECT_HOLIDAYS = "redirect:/holidays";

    private static final String VIEW_HOLIDAYS = "holidays/holidays";

    private static final String VIEW_ADD_HOLIDAY = "holidays/add_holiday";

    private static final String VIEW_UPDATE_HOLIDAY = "holidays/update_holiday";

    private static final String ATTRIBUTE_HOLIDAYS = "holidays";

    private static final String ATTRIBUTE_HOLIDAY_DTO = "holidayDto";

    private static final String FIELD_DAY = "day";

    private static final String ERROR_DURING_HOLIDAY_SAVING_MSG = "Erreur lors de l'enregistrement - Champ incorrect (la date doit être unique)";

    private static final String VERSION = "version";

    @Autowired
    private HolidayService holidayService;

    @Value("${info.app.version}")
    String applicationVersion;

    @GetMapping("")
    public ModelAndView getHolidays(Model model) {
        ModelAndView modelAndView =  new ModelAndView(VIEW_HOLIDAYS);
        modelAndView.addObject(ATTRIBUTE_HOLIDAYS, holidayService.getHolidays());
        modelAndView.addObject(VERSION,applicationVersion);
        return modelAndView;
    }

    @GetMapping("/addholiday")
    public ModelAndView addHoliday() {
        ModelAndView modelAndView = new ModelAndView(VIEW_ADD_HOLIDAY);
        HolidayDto attributeValue = new HolidayDto();
        modelAndView.addObject(ATTRIBUTE_HOLIDAY_DTO, attributeValue);
        modelAndView.addObject(VERSION,applicationVersion);
        return modelAndView;
    }

    @PostMapping("/addholiday")
    public ModelAndView addHoliday(@Valid @ModelAttribute(value = ATTRIBUTE_HOLIDAY_DTO) HolidayDto holidayDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView(VIEW_ADD_HOLIDAY);
            modelAndView.addObject(ATTRIBUTE_HOLIDAY_DTO, holidayDto);
            return modelAndView;
        }

        try {
            holidayService.save(holidayDto);
        } catch (Exception e) {
            bindingResult.addError(new FieldError(ATTRIBUTE_HOLIDAY_DTO, FIELD_DAY, ERROR_DURING_HOLIDAY_SAVING_MSG));
            return new ModelAndView(VIEW_ADD_HOLIDAY);
        }
        return new ModelAndView(REDIRECT_HOLIDAYS);
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateHoliday(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        HolidayDto holidayDto = holidayService.getHolidayDtoById(id);

        if(holidayDto != null){
            modelAndView.setViewName(VIEW_UPDATE_HOLIDAY);
            modelAndView.addObject(ATTRIBUTE_HOLIDAY_DTO, holidayDto);

        } else {
            modelAndView.setViewName(REDIRECT_HOLIDAYS);
            populateReturnError(id, redirectAttributes);
        }
        modelAndView.addObject(VERSION,applicationVersion);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateHoliday(@PathVariable("id") long id, @Valid @ModelAttribute(value= ATTRIBUTE_HOLIDAY_DTO) HolidayDto holidayDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){

        Holiday holiday = holidayService.getHolidayById(id);
        if(holiday == null){
            populateReturnError(id, redirectAttributes);
            return new ModelAndView(REDIRECT_HOLIDAYS);
        }

        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView(VIEW_UPDATE_HOLIDAY);
            modelAndView.addObject(ATTRIBUTE_HOLIDAY_DTO, holidayDto);
            return modelAndView;
        }

        try {
            holidayService.updateHoliday(holidayDto);
        } catch (Exception e) {
            bindingResult.addError(new FieldError(ATTRIBUTE_HOLIDAY_DTO, FIELD_DAY, ERROR_DURING_HOLIDAY_SAVING_MSG));
            return   new ModelAndView(VIEW_ADD_HOLIDAY);
        }

        return new ModelAndView(REDIRECT_HOLIDAYS);

    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteHoliday(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttribute){

        ModelAndView modelAndView = new ModelAndView(REDIRECT_HOLIDAYS);
        Holiday holiday = holidayService.getHolidayById(id);

        if(holiday != null){
            holidayService.deleteHolidayById(id);
        }
        else {
            populateReturnError(id, redirectAttribute);
        }

        return modelAndView;

    }

    private void populateReturnError(Long id, RedirectAttributes redir) {
        redir.addFlashAttribute("error", true);
        redir.addFlashAttribute("errorMessage", "Attention, Id Jour ferie ou chome invalide : " + id);
    }

}
