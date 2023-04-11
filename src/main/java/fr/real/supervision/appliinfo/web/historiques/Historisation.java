package fr.real.supervision.appliinfo.web.historiques;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Entity
@Table(name = "historisation")
public class Historisation implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = SEQUENCE, generator = "HISTORISATION_SEQ_GENERATOR")
    @SequenceGenerator(name = "HISTORISATION_SEQ_GENERATOR", sequenceName = "HISTORISATION_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "id_mail")
    private long idMail;
    @Column(name = "objet", length = 200)
    private String objet;
    @Column(name = "distinataires", length = 250)
    private String distinataires;
    @Column(name = "SECTION_HISTORISATION", nullable = false)
    private Long sectionHistorisation;

    @Column(name = "LA_UNE_HISTORISATION")
    private String aLaUneHistorisation;

    @Column(name = "ICONE_HISTORISATION", length = 25)
    private String iconeHistorisation;

    @Column(name = "DIRECTION_ACTIVITE_HISTORISATION")
    private String directionActiviteHistorisation;

    @Column(name = "DIRECTION_RELATION_CLIENT_HISTORISATION")
    private String directionRelationClientHistorisation;

    @Column(name = "AGENDA_PERIODE_HISTORISATION")
    private String agendaPeriodeHistorisation;

    @Column(name = "AGENDA_PERIMETRE_HISTORISATION")
    private String agendaPerimetreHistorisation;

    @Column(name = "AGENDA_NATURE_HISTORISATION")
    private String agendaNatureInterventionHistorisation;

    @Column(name = "AGENDA_IMPACT_HISTORISATION")
    private String agendaImpactHistorisation;

    @Column(name = "date_Envoi")
    private LocalDateTime dateEnvoi;
}
