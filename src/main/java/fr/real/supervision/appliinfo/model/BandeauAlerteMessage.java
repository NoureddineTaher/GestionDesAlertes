package fr.real.supervision.appliinfo.model;

import java.io.Serializable;

public class BandeauAlerteMessage extends AbstractAuditingEntity implements Serializable {
    public String bandeauAlertemesssage;

    public String getBandeauAlertemesssage() {
        return bandeauAlertemesssage;
    }

    public void setBandeauAlertemesssage(String bandeauAlertemesssage) {
        this.bandeauAlertemesssage = bandeauAlertemesssage;
    }
}
