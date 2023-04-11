package fr.real.supervision.appliinfo.web.contacts.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import fr.real.supervision.appliinfo.model.DiffusionGroup;

public class ContactDto {

	private Long id;

	@NotBlank(message = "le nom est obligatoire")
	private String name;

	@NotBlank(message = "le prenom est obligatoire")
	private String firstname;

	@NotBlank(message = "Le mail est obligatoire")
	@Pattern(regexp = "(^[a-zA-Z0-9_!#$%&'*+\\/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$)", message = "Le mail est incorrect")
	private String email;

	@NotBlank(message = "Le telephone est obligatoire")
	@Pattern(regexp = "([0][0-9]{9})", message = "Le numero de telephone est incorrect")
	private String phone;

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
