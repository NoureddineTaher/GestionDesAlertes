package fr.real.supervision.appliinfo.web.category.dto;

import javax.validation.constraints.NotBlank;

public class CategoryDto {

	private Long id;

	@NotBlank(message = "le nom est obligatoire")
	private String name;

	private String description;

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

}
