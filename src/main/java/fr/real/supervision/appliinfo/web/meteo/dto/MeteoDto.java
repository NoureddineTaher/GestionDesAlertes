package fr.real.supervision.appliinfo.web.meteo.dto;


import fr.real.supervision.appliinfo.model.Meteo;
import lombok.*;

import java.util.List;
@Getter
@Setter
public class MeteoDto {

    private List<Meteo> section1;

    private List<Meteo> section2;

    private List<Meteo> section3;

    private List<Meteo> section4;

    private String subjectMail;

    private String to;


    private String from;

}
