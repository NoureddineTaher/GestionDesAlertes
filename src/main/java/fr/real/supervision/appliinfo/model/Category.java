package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Category extends AbstractAuditingEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "CATEGORY_SEQ_GENERATOR")
	@SequenceGenerator(name = "CATEGORY_SEQ_GENERATOR", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column
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

	@Override
	public String toString() {
		return "Category [" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				']';
	}
}
