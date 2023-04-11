package fr.real.supervision.appliinfo.web.historiques;


import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.web.meteo.dto.MailDto;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistorisationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistorisationService.class);
    private final HistorisationRepository historisationRepository;

    public HistorisationService(HistorisationRepository historisationRepository) {
        this.historisationRepository = historisationRepository;
    }

    public Historisation saveHisto(MailDto mailDto) {
        Historisation historisation = new Historisation();
        historisation.setDistinataires(mailDto.getTo());
        historisation.setObjet(mailDto.getSubjectMail());
        historisation.setDateEnvoi(LocalDateTime.now());
        return historisationRepository.save(historisation);
    }

    public void saveHistoSections(MeteoDto meteoDto, long id) {
        Historisation historisation = new Historisation();
        historisation.setIdMail(id);
        historisation.setDateEnvoi(LocalDateTime.now());
        List<Historisation> meteoList = populateHistorisationFromMeteoDtoToHistorisation(meteoDto, id);
        historisationRepository.saveAll(meteoList);
        LOGGER.info("Enregistrement  de {}", meteoList);

    }

    private List<Historisation> populateHistorisationFromMeteoDtoToHistorisation(MeteoDto meteoDto, long id) {

        List<Historisation> historisationslist = new ArrayList<>();

        List<Meteo> meteoList = new ArrayList<>(meteoDto.getSection1());
        meteoList.addAll(meteoDto.getSection2());
        meteoList.addAll(meteoDto.getSection4());
        if (meteoDto.getSection3() != null) {

            meteoList.addAll(meteoDto.getSection3());
        }
        for (Meteo meteo : meteoList) {
            historisationslist.add(fromMeteoToHistorisation(meteo, id));
        }
        return historisationslist;
    }

    private Historisation fromMeteoToHistorisation(Meteo meteo, long id) {
        Historisation historisation = new Historisation();
        historisation.setSectionHistorisation(meteo.getSection());
        //taguer la section avec l'id du mail dans le champs idMail
        historisation.setIdMail(id);
        historisation.setSectionHistorisation(meteo.getSection());
        historisation.setAgendaPeriodeHistorisation(meteo.getAgendaPeriode());
        historisation.setAgendaPerimetreHistorisation(meteo.getAgendaPerimetre());
        historisation.setDirectionActiviteHistorisation(meteo.getDirectionActivite());
        historisation.setDirectionRelationClientHistorisation(meteo.getDirectionRelationClient());
        historisation.setIconeHistorisation(meteo.getIcone());
        historisation.setAgendaImpactHistorisation(meteo.getAgendaImpact());
        historisation.setALaUneHistorisation(meteo.getALaUne());
        historisation.setDateEnvoi(LocalDateTime.now());
        return historisation;
    }
}
