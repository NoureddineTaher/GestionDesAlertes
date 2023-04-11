package fr.real.supervision.appliinfo.web;

import fr.real.supervision.appliinfo.web.BandeauAlerte.service.BandeauTextService;
import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class AlertControllerAdvice {

    private final BandeauTextService bandeauTextService;
    private final DiffusionGroupService diffusionGroupService;

    public AlertControllerAdvice(BandeauTextService bandeauTextService, DiffusionGroupService diffusionGroupService) {
        this.bandeauTextService = bandeauTextService;
        this.diffusionGroupService = diffusionGroupService;
    }

    @Value("${info.app.version}")
    String applicationVersion;

    @ModelAttribute("lastMiseAjourDateAppReseau")
    public String populatelastMiseAjourDateAppReseau() {
        return bandeauTextService.getLastMiseAjourDateAppReseau();

    }

    @ModelAttribute("lastMiseAjourDateInfraConfiance")
    public String populatelastMiseAjourDateInfraConfiance() {
        return bandeauTextService.getLastMiseAjourDateInfraConfiance();

    }

    @ModelAttribute("lastMiseAjourDateAppMetiers")
    public String populatelastMiseAjourDateAppMetiers() {
        return bandeauTextService.getLastMiseAjourDateAppMetiers();

    }

    @ModelAttribute("alertText")
    public String populateAlertText() {
       return bandeauTextService.getBandeauText();

    }
    @ModelAttribute("miseAjourText")
    public String populateMiseaJourText() {
        return bandeauTextService.getBandeauMiseAjourText();

    }
    @ModelAttribute("cloture")
    public boolean populateCloture() {
        return bandeauTextService.isCloture();

    }

    @ModelAttribute("applicationVersion")
    public String populateVersion() {
        return applicationVersion;

    }



}
