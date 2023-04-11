package fr.real.supervision.appliinfo.web.incidents.controller;


import fr.real.supervision.appliinfo.web.incidents.dto.IncidentDto;
import fr.real.supervision.appliinfo.web.incidents.models.Incident;
import fr.real.supervision.appliinfo.web.incidents.service.EmailSenderIncidentService;
import fr.real.supervision.appliinfo.web.incidents.service.IncidentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.util.Optional;

@Controller
public class IncidentsController {
    private static final String REDIRECT_PRELOOK = "incidents/incidentPrelook";

    private static final String REDIRECT_INCIDENT = "redirect:/incident-creation/";
    private static final String INCIDENT= "incident";
    private final EmailSenderIncidentService emailSenderServiceIncident;
    private final IncidentService incidentService;
    public IncidentsController(IncidentService incidentService, EmailSenderIncidentService emailSenderServiceIncident) {
        this.incidentService = incidentService;
        this.emailSenderServiceIncident = emailSenderServiceIncident;

    }

    @GetMapping("/incident-creation/{num}")
    public ModelAndView getIncidentCreationPage(@PathVariable("num") int num) {
        IncidentDto incident = new IncidentDto();
        int incidentNumber = num;
        incident.getId();
        ModelAndView modelAndView = new ModelAndView("incidents/incidentCommunication");
        modelAndView.addObject("incidentNumber", incidentNumber);
        modelAndView.addObject(INCIDENT, incident);
        return modelAndView;
    }

    @PostMapping("/incidents/incidentPrelook/{num}")
    public ModelAndView prelookIncident(@PathVariable("num") int num, @ModelAttribute("incident") IncidentDto incident) {
        incident.setIncidentNumber(num);
        ModelAndView modelAndView = new ModelAndView(REDIRECT_PRELOOK);
        Incident incidentSaved = incidentService.save(incident);
        incident.setId(incidentSaved.getId());
        String[] destinationList= incident.getIncidentDestination().split(" ");
        modelAndView.addObject("destinationList" , destinationList);
        modelAndView.addObject(INCIDENT, incident);
        return modelAndView;
    }

    @PostMapping("/incidents/send/{id}")
    public ModelAndView sendIncident(@PathVariable("id") long id, @ModelAttribute("mailIncidentdto") IncidentDto mailIncidentdto) throws MessagingException {
        Optional<Incident> incident = incidentService.getIncidentById(id);
        emailSenderServiceIncident.sendEmail(incident.get());
        incidentService.deleteIncidentCloture(incident.get());
        return new ModelAndView(REDIRECT_INCIDENT+id);

    }

    @GetMapping ("/incident-follow/{num}")
    public ModelAndView getIncidentSuiviPage(@PathVariable("num") int num) {


        ModelAndView modelAndView = new ModelAndView("incidents/suivi");
        Optional<Incident> incident = incidentService.getIncidentById((long) num);
        if(incident.isPresent()){
            modelAndView.addObject(INCIDENT, incident.get());
            modelAndView.addObject("incidentNumber", num);
            return modelAndView;
        }
        else{
            return new ModelAndView(REDIRECT_INCIDENT+num);
        }

    }


}