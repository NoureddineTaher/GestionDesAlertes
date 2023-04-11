package fr.real.supervision.appliinfo.web.status.controller;

import fr.real.supervision.appliinfo.web.BandeauAlerte.service.BandeauTextService;
import fr.real.supervision.appliinfo.web.status.service.MeteoStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class MeteoStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeteoStatusController.class);

    private final MeteoStatusService meteoStatusService;
    private final BandeauTextService bandeauTextService;
    private final String[] adsnDomains = {"Applications Métier","Infrastructure de confiance","Applications Réseau"};

    @GetMapping("/information/status")
    public ModelAndView getMeteoStatus(@RequestParam final String domain) {
        String fullDomain = new String();
        ModelAndView modelAndView = new ModelAndView("information");

        switch (domain){
            case("adsn"):
                fullDomain = "ADSN";
                break;
            case("dex-sieo"):
                fullDomain = "Direction de l’Exploitation – Service Intégration et Exploitation des Outils";
                break;
            case("dex-siei"):
                fullDomain = "Direction de l’Exploitation – Service Intégration et Exploitation des Infrastructures";
                break;
            case ("pole-numerique"):
                fullDomain = "ADSN";
        }


        LOGGER.info(String.format("Retrieve alerts for domain  %s", fullDomain));

        modelAndView.addObject("domain", fullDomain);
        modelAndView.addObject("StatusByToolAndSeverityListByThree",  meteoStatusService.getMeteoByThree(fullDomain));
        modelAndView.addObject("ADSNcolumns",  this.adsnDomains);


        return modelAndView;

    }


    @GetMapping("/information/status/updateLastMiseAjourDate")
    @ResponseBody
    public String updateLastMiseAjourDate(@RequestParam final String domain, Model model) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (Objects.equals(domain, "Applications Réseau")) {

            Date date = new Date(System.currentTimeMillis());
            bandeauTextService.setLastMiseAjourDateAppReseau(formatter.format(date));
            model.addAttribute("lastMiseAjourDateAppReseau", bandeauTextService.getLastMiseAjourDateAppReseau());
            return bandeauTextService.getLastMiseAjourDateAppReseau();
        }
        if (Objects.equals(domain, "Applications Métier")) {
            Date date = new Date(System.currentTimeMillis());
            bandeauTextService.setLastMiseAjourDateAppMetiers(formatter.format(date));
            model.addAttribute("lastMiseAjourDateAppMetiers", bandeauTextService.getLastMiseAjourDateAppMetiers());
            return bandeauTextService.getLastMiseAjourDateAppMetiers();
        }
        if (Objects.equals(domain, "Infrastructure de confiance")) {
            Date date = new Date(System.currentTimeMillis());
            bandeauTextService.setLastMiseAjourDateInfraConfiance(formatter.format(date));
            model.addAttribute("lastMiseAjourDateInfraConfiance", bandeauTextService.getLastMiseAjourDateInfraConfiance());
            return bandeauTextService.getLastMiseAjourDateInfraConfiance();
        }
        return null;
    }
}
