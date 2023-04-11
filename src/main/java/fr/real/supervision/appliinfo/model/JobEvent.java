package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class JobEvent {
	
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "JOB_EVENT_SEQ_GENERATOR")    
    @SequenceGenerator(name = "JOB_EVENT_SEQ_GENERATOR", sequenceName = "JOB_EVENT_SEQ", allocationSize = 1)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private JobType type;

	@Column(name="BEGIN")
	private LocalDateTime start;
	
	@Column
	private LocalDateTime end;

	@Column
	private Boolean success;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JobType getType() {
		return type;
	}

	public void setType(JobType type) {
		this.type = type;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}
