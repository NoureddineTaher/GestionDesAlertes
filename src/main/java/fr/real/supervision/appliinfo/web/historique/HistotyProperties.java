package fr.real.supervision.appliinfo.web.historique;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties("historique")
@Validated
public class HistotyProperties {

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    private String period;


}
