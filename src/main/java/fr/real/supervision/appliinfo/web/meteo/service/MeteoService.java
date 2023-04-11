package fr.real.supervision.appliinfo.web.meteo.service;

import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;

import java.util.List;

public interface MeteoService {


    Meteo getMeteoById(Long id);

    public List<Meteo> getMeteos();

    void deleteMeteoById();

    void saveMeteoSection3();

    void saveMeteoDto(MeteoDto meteosDto);

    public List<Meteo> getMeteoBySection(Long section);

}
