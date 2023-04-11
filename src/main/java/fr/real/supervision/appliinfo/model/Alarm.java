package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;

@Entity
@Table(name = "ALARM")
public class Alarm  extends  AbstractAuditingEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "ALARM_SEQ_GENERATOR")
	@SequenceGenerator(name = "ALARM_SEQ_GENERATOR", sequenceName = "ALARM_SEQ", allocationSize = 1)
	private Long id;

	/**
	 * Nom de l'alarm dans ITM
	 */
	@Column(nullable = false)
	private String name;

	@Column
	private String description;

	@Column(nullable = false)
	@Max(value = 100, message = "Le poids ne peut pas dépasser 100")
	private Integer weight;

	@Column(nullable = true)
	private String server;

	/**
	 * Liste des alertes qui contiennent cette alarme
	 */
	@ManyToMany(mappedBy = "alarms")
	private Set<Alert> alerts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Set<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(Set<Alert> alerts) {
		this.alerts = alerts;
	}


	@Override
	public String toString() {
		return "Alarme [ " +  ", nom ='" + this.name + '\'' + ", description='" + this.description + '\'' + ",poids=" + this.weight + ", server='" + this.server  + ']';
	}
}
