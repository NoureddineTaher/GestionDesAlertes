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
import javax.validation.constraints.NotBlank;

@Entity
public class DiffusionGroup extends AbstractAuditingEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "DIFFUSION_GROUP_SEQ_GENERATOR")
	@SequenceGenerator(name = "DIFFUSION_GROUP_SEQ_GENERATOR", sequenceName = "DIFFUSION_GROUP_SEQ", allocationSize = 1)
	private Long id;
	
	@Column(unique = true, nullable=false)
	@NotBlank(message = "Le nom est obligatoire")
	private String name;

	@Column
	private String description;
	
	@ManyToMany
	private Set<Contact> contacts;

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

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public String toString(){ return this.getName(); }
}
