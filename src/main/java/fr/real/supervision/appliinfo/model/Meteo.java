package fr.real.supervision.appliinfo.model;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Getter
@Setter
@Entity
@Table(name = "METEO")
public class Meteo implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "METEO_SEQ_GENERATOR")
	@SequenceGenerator(name = "METEO_SEQ_GENERATOR", sequenceName = "METEO_SEQ", allocationSize = 1)
	private long id;

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
	// @Max(value = 150, message = "Le périmètre ne peut pas dépasser 150")
	private String agendaPerimetre;

	@Column(name = "AGENDA_NATURE_INTERVENTION")
	// @Max(value = 40, message = "La nature ne peut pas dépasser 40")
	private String agendaNatureIntervention;

	@Column(name = "AGENDA_IMPACT")
	// @Max(value = 90, message = "L'impact' ne peut pas dépasser 90")
	private String agendaImpact;


}