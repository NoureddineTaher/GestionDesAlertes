package fr.real.supervision.appliinfo.web.BandeauAlerte.Controller;

import fr.real.supervision.appliinfo.model.BandeauAlerteMessage;
import fr.real.supervision.appliinfo.model.BandeauAlerteMiseAjour;
import fr.real.supervision.appliinfo.web.BandeauAlerte.service.BandeauTextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class BandeauAlerteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BandeauAlerteController.class);
    private final BandeauTextService bandeauTextService;

    public BandeauAlerteController(BandeauTextService bandeauTextService) {
        this.bandeauTextService = bandeauTextService;
    }


    @GetMapping("/clotureSuccess")
    @ResponseStatus(value= HttpStatus.OK)
    public void cloture(Model model) {
        if(bandeauTextService.getBandeauMiseAjourText()!=null) {
            LOGGER.info("Clôture du bandeau d'alerte :{}", bandeauTextService.getBandeauMiseAjourText());
        }
        else{
            LOGGER.info("Clôture du bandeau d'alerte :{}", bandeauTextService.getBandeauText());
        }
        bandeauTextService.setCloture(true);
        bandeauTextService.setBandeauText(null);
        bandeauTextService.setBandeauMiseAjourText(null);

    }


    @GetMapping("/bdalerte/{bdaItem}")
    public String viewBandeau(@PathVariable("bdaItem") String bdaItem, Model model) {
        model.addAttribute("bdaItem", bdaItem);
        model.addAttribute("bandeauAlerteMessage", new BandeauAlerteMessage());
        model.addAttribute("bandeauAlerteMiseAjour", new BandeauAlerteMiseAjour());
        model.addAttribute("alertText", bandeauTextService.getBandeauText());
        model.addAttribute("miseAjourText", bandeauTextService.getBandeauMiseAjourText());
        return "bandeauAlerte/" + bdaItem;
    }


    @PostMapping("/bdalerte/creationFormSubmit")
    public String createAlerte(@ModelAttribute BandeauAlerteMessage bandeauAlerteMessage, Model model) {

        bandeauTextService.setBandeauText(bandeauAlerteMessage.bandeauAlertemesssage);
        bandeauTextService.setBandeauMiseAjourText(null);
        bandeauTextService.setCloture(false);
        model.addAttribute("cloture", bandeauTextService.isCloture());
        model.addAttribute("alertText", bandeauTextService.getBandeauText());
        model.addAttribute("miseAjourText", bandeauTextService.getBandeauMiseAjourText());
        LOGGER.info("Création du bandeau d'alerte : {}",bandeauAlerteMessage.bandeauAlertemesssage);
        return "bandeauAlerte/formSubmit";
    }

    @PostMapping("/bdalerte/miseAjourFormSubmit")
    public String createAlerteMiseAjour(@ModelAttribute BandeauAlerteMiseAjour bandeauAlerteMiseAjour, Model model) {

        bandeauTextService.setBandeauMiseAjourText(bandeauAlerteMiseAjour.bandeauAlertemiseAjour);
        bandeauTextService.setBandeauText(null);
        bandeauTextService.setCloture(false);
        model.addAttribute("miseAjourText",bandeauTextService.getBandeauMiseAjourText()) ;
        model.addAttribute("alertText", bandeauTextService.getBandeauText());
        LOGGER.info("mise ajour du bandeau d'alerte : {}", bandeauAlerteMiseAjour.bandeauAlertemiseAjour);
        return "bandeauAlerte/formSubmit";

    }

}