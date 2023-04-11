package fr.real.supervision.appliinfo.web.historiques;

import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.web.meteo.dto.MailDto;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HistorisationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistorisationController.class);
    private final HistorisationRepository historisationRepository;

    public HistorisationController(HistorisationRepository historisationRepository) {

        this.historisationRepository = historisationRepository;
    }

    @GetMapping("/meteo/historique")
    public ModelAndView getHistorisationPage(@ModelAttribute("mailDto") MailDto mailDto) {
        ModelAndView modelAndView = new ModelAndView("historiques/historiqueMeteo");
        List<Historisation> historisationList = new ArrayList<>();
        Historisation historisation = historisationRepository.findTop1ByIdMailOrderByDateEnvoiDesc(0);

        if (historisation != null) {
            historisationList.addAll(historisationRepository.findByIdMail(historisation.getId()));
        }
        MeteoDto meteoDto = new MeteoDto();
        meteoDto.setTo(historisation.getDistinataires());
        meteoDto.setSubjectMail(historisation.getObjet());
        List<Meteo> section1 = new ArrayList<>();
        List<Meteo> section2 = new ArrayList<>();
        List<Meteo> section3 = new ArrayList<>();
        List<Meteo> section4 = new ArrayList<>();
        for (Historisation historisationMeteo : historisationList) {
            if (historisationMeteo.getSectionHistorisation() != null && historisationMeteo.getSectionHistorisation() == 1) {
                section1.add(fromHistorisationToMeteoDtoSection1(historisationMeteo));
                meteoDto.setSection1(section1);
            }
        }
        for (Historisation historisationMeteo : historisationList) {
            if (historisationMeteo.getSectionHistorisation() != null && historisationMeteo.getSectionHistorisation() == 2) {
                section2.add(fromHistorisationToMeteoDtoSection1(historisationMeteo));
                meteoDto.setSection2(section2);
            }
        }
        for (Historisation historisationMeteo : historisationList) {
            if (historisationMeteo.getSectionHistorisation() != null && historisationMeteo.getSectionHistorisation() == 3) {
                section3.add(fromHistorisationToMeteoDtoSection1(historisationMeteo));
                meteoDto.setSection3(section3);
            }
        }
        for (Historisation historisationMeteo : historisationList) {
            if (historisationMeteo.getSectionHistorisation() != null && historisationMeteo.getSectionHistorisation() == 4) {
                section4.add(fromHistorisationToMeteoDtoSection1(historisationMeteo));
                meteoDto.setSection4(section4);
            }
        }
        String[] destinationList = meteoDto.getTo().split(" ");
        modelAndView.addObject("destinationList", destinationList);
        modelAndView.addObject("meteodto", meteoDto);
        LOGGER.info("historisation de la meteo ");
        return modelAndView;
    }

    private Meteo fromHistorisationToMeteoDtoSection1(Historisation historisationMeteo) {
        Meteo meteo = new Meteo();
        meteo.setId(historisationMeteo.getId());
        meteo.setSection(historisationMeteo.getSectionHistorisation());
        meteo.setALaUne(historisationMeteo.getALaUneHistorisation());
        meteo.setDirectionRelationClient(historisationMeteo.getDirectionRelationClientHistorisation());
        meteo.setIcone(historisationMeteo.getIconeHistorisation());
        meteo.setAgendaImpact(historisationMeteo.getAgendaImpactHistorisation());
        meteo.setDirectionActivite(historisationMeteo.getDirectionActiviteHistorisation());
        meteo.setAgendaNatureIntervention(historisationMeteo.getAgendaNatureInterventionHistorisation());
        meteo.setAgendaPerimetre(historisationMeteo.getAgendaPerimetreHistorisation());
        meteo.setAgendaPeriode(historisationMeteo.getAgendaPeriodeHistorisation());
        return meteo;
    }
}