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

@Entity
public class Contact extends  AbstractAuditingEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "CONTACT_SEQ_GENERATOR")
	@SequenceGenerator(name = "CONTACT_SEQ_GENERATOR", sequenceName = "CONTACT_SEQ", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String firstname;

	@Column
	private String email;

	@Column
	private String phone;

	@ManyToMany(mappedBy = "contacts")
	private Set<DiffusionGroup> diffusionGroups;

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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<DiffusionGroup> getDiffusionGroups() {
		return diffusionGroups;
	}

	public void setDiffusionGroups(Set<DiffusionGroup> diffusionGroups) {
		this.diffusionGroups = diffusionGroups;
	}

}
