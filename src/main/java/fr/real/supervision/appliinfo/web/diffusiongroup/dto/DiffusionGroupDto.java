package fr.real.supervision.appliinfo.web.diffusiongroup.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import fr.real.supervision.appliinfo.model.Contact;

public class DiffusionGroupDto {

	private Long id;

	@NotBlank(message = "Le nom est obligatoire")
	private String name;

	private String description;

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

}
