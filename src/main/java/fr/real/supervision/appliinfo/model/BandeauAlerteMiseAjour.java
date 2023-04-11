package fr.real.supervision.appliinfo.model;

import java.io.Serializable;

public class BandeauAlerteMiseAjour extends AbstractAuditingEntity implements Serializable {
    public String bandeauAlertemiseAjour;

    public String getBandeauAlertemiseAjour() {
        return bandeauAlertemiseAjour;
    }

    public void setBandeauAlertemiseAjour(String bandeauAlertemiseAjour) {
        this.bandeauAlertemiseAjour = bandeauAlertemiseAjour;
    }
}

