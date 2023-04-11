package fr.real.supervision.appliinfo.web.historiques;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorisationDto {
    private long id;
    private long idMail;
    private String objet;
    private String distinataires;
    private Long section;
    private String aLaUne;
    private String icone;
    private String directionActivite;
    private String directionRelationClient;
    private String agendaPeriode;
    private String agendaPerimetre;
    private String agendaNatureIntervention;
    private String agendaImpact;
    private LocalDateTime envoiDate;
}
