package fr.real.supervision.appliinfo.web.meteo.controller;


import javax.mail.MessagingException;
import javax.validation.Valid;


import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.web.historiques.HistorisationService;
import fr.real.supervision.appliinfo.web.meteo.dto.MailDto;
import fr.real.supervision.appliinfo.web.meteo.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;
import fr.real.supervision.appliinfo.web.meteo.service.MeteoService;

import java.text.SimpleDateFormat;
import java.time.Instant;

import static java.util.Date.from;


@Controller
public class MeteoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeteoController.class);
    private static final String VIEW_METEO = "meteo/meteo";
    private static final String REDIRECT_METEO  = "redirect:/meteos";
    private static final String REDIRECT_PRELOOK  = "meteo/prelook";

    private static final String ATTRIBUTE_METEO_DTO = "meteodto";
    private static final String ATTRIBUTE_MAIL_DTO = "maildto";
    private static final String ATTRIBUTE_LIST_DIFFUSIONGROUPS = "listDiffusionGroups";

	private static final String VERSION = "version";


    private final MeteoService meteoService;
    private final DiffusionGroupService diffusionGroupService;
    private final EmailSenderService emailSenderService;
    private final HistorisationService historisationService;

    @Value("${info.app.version}")
    String applicationVersion;

    public MeteoController(MeteoService meteoService, DiffusionGroupService diffusionGroupService, EmailSenderService emailSenderService, HistorisationService historisationService ) {
        this.meteoService = meteoService;
        this.diffusionGroupService = diffusionGroupService;
        this.emailSenderService = emailSenderService;
        this.historisationService=historisationService;
    }
    @GetMapping("/meteos")
    public ModelAndView getMeteos() {
        ModelAndView modelAndView = new ModelAndView(VIEW_METEO);
        modelAndView.addObject(VERSION,applicationVersion);
        modelAndView.addObject(ATTRIBUTE_METEO_DTO, getMeteoDTO());

         return modelAndView;
    }

    @GetMapping("/meteos/delete")
    public ModelAndView deleteMeteo() {

        ModelAndView modelAndView = new ModelAndView(REDIRECT_METEO);
        meteoService.deleteMeteoById();
        return modelAndView;

    }

    @RequestMapping("/meteos/addameteo")
    public ModelAndView addMeteo() {

        meteoService.saveMeteoSection3();
        return new ModelAndView(REDIRECT_METEO);
    }

    @PostMapping("/meteos/send")
    public ModelAndView sendMeteo(@ModelAttribute("meteoDto") MailDto mailDto) throws MessagingException {
        emailSenderService.sendEmail(mailDto);
        return new ModelAndView(REDIRECT_METEO);
    }
    @PostMapping(value = "/meteos/saveall")
    public ModelAndView saveAll(@Valid  @ModelAttribute(value= ATTRIBUTE_METEO_DTO) MeteoDto meteoDto, @RequestParam(value="action") String action) {

        meteoService.saveMeteoDto(meteoDto);
        LOGGER.info("Enregistrement du {}", meteoDto);
        if(action.equals("prelook")) {
            LOGGER.info("Prévisualisation de la page ");
            return new ModelAndView("redirect:/meteos/prelook" );

        }

        if(action.equals("addameteo")) {
            LOGGER.info("Ajout d'une méteo");
            return new ModelAndView("redirect:/meteos/addameteo" );
        }

        if(action.equals("delete")) {
            LOGGER.info("Suppression d'une méteo");
            return new ModelAndView("redirect:/meteos/delete" );
        }
        return new ModelAndView(REDIRECT_METEO);
    }
    @GetMapping("/meteos/prelook")
    public ModelAndView prelook() {
        ModelAndView modelAndView = new ModelAndView(REDIRECT_PRELOOK);

        modelAndView.addObject(VERSION,applicationVersion);
        MeteoDto meteoDto = getMeteoDTO();
        meteoDto.setTo(diffusionGroupService.getDiffusionGroups().toString());
        meteoDto.setFrom("@DEX.GDS");
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(from(Instant.now()));
        LOGGER.info("Date : {}  ",date);
        String subject = "Météo des applications - " + date;
        meteoDto.setSubjectMail(subject);
        LOGGER.info("Sujet du mail : {}  ",subject);
        modelAndView.addObject(ATTRIBUTE_METEO_DTO, meteoDto);
        return  modelAndView;
    }


    private MeteoDto getMeteoDTO()  {


        MeteoDto meteodto = new MeteoDto();
        meteodto.setSection1(meteoService.getMeteoBySection(1L));
        meteodto.setSection2(meteoService.getMeteoBySection(2L));
        meteodto.setSection3(meteoService.getMeteoBySection(3L));
        meteodto.setSection4(meteoService.getMeteoBySection(4L));
        return meteodto;
    }
}
