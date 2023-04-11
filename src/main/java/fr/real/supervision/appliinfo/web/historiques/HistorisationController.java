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
        Historisation historisation1 = historisationRepository.findTop1ByIdMailOrderByDateEnvoiDesc(0);

        if (historisation1 != null) {
            historisationList.addAll(historisationRepository.findByIdMail(historisation1.getId()));
        }

        MeteoDto meteoDto = new MeteoDto();
        meteoDto.setTo(historisation1.getDistinataires());
        meteoDto.setSubjectMail(historisation1.getObjet());

        List<Meteo> section1 = new ArrayList<>();
        List<Meteo> section2 = new ArrayList<>();
        List<Meteo> section3 = new ArrayList<>();
        List<Meteo> section4 = new ArrayList<>();
        for (Historisation historisation : historisationList) {
            if (historisation.getSection() != null && historisation.getSection() == 1) {
                section1.add(fromHistorisationToMeteoDtoSection1(historisation));
                meteoDto.setSection1(section1);
            }
        }
        for (Historisation historisation : historisationList) {
            if (historisation.getSection() != null && historisation.getSection() == 2) {
                section2.add(fromHistorisationToMeteoDtoSection1(historisation));
                meteoDto.setSection2(section2);
            }
        }
        for (Historisation historisation : historisationList) {
            if (historisation.getSection() != null && historisation.getSection() == 3) {
                section3.add(fromHistorisationToMeteoDtoSection1(historisation));
                meteoDto.setSection3(section3);
            }
        }
        for (Historisation historisation : historisationList) {
            if (historisation.getSection() != null && historisation.getSection() == 4) {
                section4.add(fromHistorisationToMeteoDtoSection1(historisation));
                meteoDto.setSection4(section4);
            }
        }
        String[] destinationList = meteoDto.getTo().split(" ");
        modelAndView.addObject("destinationList", destinationList);
        modelAndView.addObject("meteodto", meteoDto);
        LOGGER.info("historisation de la meteo ");
        return modelAndView;
    }

    private Meteo fromHistorisationToMeteoDtoSection1(Historisation historisation) {
        Meteo meteo = new Meteo();
        meteo.setId(historisation.getId());
        meteo.setSection(historisation.getSection());
        meteo.setALaUne(historisation.getALaUne());
        meteo.setDirectionRelationClient(historisation.getDirectionRelationClient());
        meteo.setIcone(historisation.getIcone());
        meteo.setAgendaImpact(historisation.getAgendaImpact());
        meteo.setDirectionActivite(historisation.getDirectionActivite());
        meteo.setAgendaNatureIntervention(historisation.getAgendaNatureIntervention());
        meteo.setAgendaPerimetre(historisation.getAgendaPerimetre());
        meteo.setAgendaPeriode(historisation.getAgendaPeriode());
        return meteo;
    }
}