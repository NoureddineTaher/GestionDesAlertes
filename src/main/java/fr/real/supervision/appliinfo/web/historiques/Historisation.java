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
    @Column(name = "SECTION", nullable = false)
    private Long section;

    @Column(name = "A_LA_UNE")
    private String aLaUne;

    @Column(name = "ICONE", length = 25)
    private String icone;

    @Column(name = "DIRECTION_ACTIVITE")
    private String directionActivite;

    @Column(name = "DIRECTION_RELATION_CLIENT")
    private String directionRelationClient;

    @Column(name = "AGENDA_PERIODE")
    private String agendaPeriode;

    @Column(name = "AGENDA_PERIMETRE")
    private String agendaPerimetre;

    @Column(name = "AGENDA_NATURE_INTERVENTION")
    private String agendaNatureIntervention;

    @Column(name = "AGENDA_IMPACT")
    private String agendaImpact;

    @Column(name = "date_Envoi")
    private LocalDateTime dateEnvoi;
}
