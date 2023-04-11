package fr.real.supervision.appliinfo.web.meteo.service.impl;


import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.repository.MeteoRepository;
import fr.real.supervision.appliinfo.web.incidents.models.Incident;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;
import fr.real.supervision.appliinfo.web.meteo.service.MeteoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MeteoServiceImpl implements MeteoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeteoServiceImpl.class);

    @Autowired
    private MeteoRepository meteoRepository;

    @Override
    public Meteo getMeteoById(Long id) {
        return meteoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Meteo> getMeteoBySection(Long section) {
        return meteoRepository.findBySection(section);
    }
    @Override
    public List<Meteo> getMeteos() {
        Iterable<Meteo> findAll = meteoRepository.findAll();
        List<Meteo> findAll1 = (List<Meteo>) findAll;
        return findAll1;
    }
    @Override
    public void deleteMeteoById() {
        Long id = meteoRepository.getLastMeteo();
        if(id != null)
        {
            meteoRepository.deleteById(id);
            LOGGER.info("Suppression de l'id {} avec succes",id);
        }
    }

    @Override
    public void saveMeteoSection3() {

        Meteo meteo= new Meteo();
        meteo.setSection(3L);
        meteoRepository.save(meteo);
        LOGGER.info("L'ajout d'une ligne avec succes");
    }

    @Override
    public void saveMeteoDto(MeteoDto meteoDto) {
        List<Meteo> meteoList = populateMeteoFromMeteoDto(meteoDto);
        meteoRepository.saveAll(meteoList);
        LOGGER.info("Enregistrement  de {}", meteoList);
    }

    private List<Meteo> populateMeteoFromMeteoDto(MeteoDto meteoDto) {

        List<Meteo> meteos = new ArrayList<>(meteoDto.getSection1());
        meteos.addAll(meteoDto.getSection2());
        meteos.addAll(meteoDto.getSection4());

        if(meteoDto.getSection3() !=null){

            meteos.addAll(meteoDto.getSection3());
        }

        return meteos;
    }

}
