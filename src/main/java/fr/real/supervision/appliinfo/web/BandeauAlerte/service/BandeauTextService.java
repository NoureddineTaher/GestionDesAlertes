package fr.real.supervision.appliinfo.web.BandeauAlerte.service;

import org.springframework.stereotype.Service;

@Service
public class BandeauTextService {

    private static String bandeauText;
    private static String bandeauMiseAjourText;
    private static String lastMiseAjourDateAppMetiers;

    public static String getLastMiseAjourDateAppReseau() {
        return lastMiseAjourDateAppReseau;
    }

    public static void setLastMiseAjourDateAppReseau(String lastMiseAjourDateAppReseau) {
        BandeauTextService.lastMiseAjourDateAppReseau = lastMiseAjourDateAppReseau;
    }

    private static String lastMiseAjourDateInfraConfiance;
    private static String lastMiseAjourDateAppReseau;
    private static boolean cloture;

    public static String getLastMiseAjourDateInfraConfiance() {
        return lastMiseAjourDateInfraConfiance;
    }

    public static void setLastMiseAjourDateInfraConfiance(String lastMiseAjourDateInfraConfiance) {
        BandeauTextService.lastMiseAjourDateInfraConfiance = lastMiseAjourDateInfraConfiance;
    }



    public static String getLastMiseAjourDateAppMetiers() {
        return lastMiseAjourDateAppMetiers;
    }

    public static void setLastMiseAjourDateAppMetiers(String lastMiseAjourDateAppMetiers) {
        BandeauTextService.lastMiseAjourDateAppMetiers = lastMiseAjourDateAppMetiers;
    }

    public static boolean isCloture() {
        return cloture;
    }

    public static void setCloture(boolean cloture) {
        BandeauTextService.cloture = cloture;
    }

    public static String getBandeauMiseAjourText() {
        return bandeauMiseAjourText;
    }

    public static void setBandeauMiseAjourText(String bandeauMiseAjourText) {
        BandeauTextService.bandeauMiseAjourText = bandeauMiseAjourText;
    }

    public static String getBandeauText() {
        return bandeauText;
    }

    public static void setBandeauText(String bandeauText) {
        BandeauTextService.bandeauText = bandeauText;
    }
}

