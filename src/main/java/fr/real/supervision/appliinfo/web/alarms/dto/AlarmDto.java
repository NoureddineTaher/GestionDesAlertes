package fr.real.supervision.appliinfo.web.alarms.dto;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.web.alarms.validator.ServerRegexpConstraint;

public class AlarmDto {
	private Long id;

	@NotBlank(message = "Le nom est obligatoire")
	private String name;

	private String description;

	@NotNull
	@Max(value = 100, message = "Le poids ne peut pas dépasser 100")
	private String weight;

	@ServerRegexpConstraint
	private String server;
	
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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
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

	public boolean validateWeight(){
		return StringUtils.isNumeric(this.weight);
		}

}
