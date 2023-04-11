package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class AlertEvent implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "ALERT_EVENT_SEQ_GENERATOR")    
    @SequenceGenerator(name = "ALERT_EVENT_SEQ_GENERATOR", sequenceName = "ALERT_EVENT_SEQ", allocationSize = 1) 
	private Long id;

	@Enumerated(EnumType.STRING)
	private AlertEventStatus status;

	@ManyToOne
	private Alert alert;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	private List<AlarmEvent> alarmEvents;

	@Column(name="BEGIN")
	private LocalDateTime start;

	@Column(name="ALERT_EVENT_END")
	private LocalDateTime alertEventEnd;

	@Column
	private LocalDateTime lastUpdate;
	
	@Column
	private Integer weightSum;

	@Column
	private LocalDateTime startSmsSent;

	@Column
	private LocalDateTime endSmsSent;	
	
	@Column
	private LocalDateTime startMailSent;

	@Column
	private LocalDateTime endMailSent;	
	
	@Column
	private LocalDateTime astreinteSent;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}


	public LocalDateTime getAlertEventEnd() {
		return alertEventEnd;
	}

	public void setAlertEventEnd(LocalDateTime end) {
		this.alertEventEnd = end;
	}

	public Integer getWeightSum() {
		return weightSum;
	}

	public void setWeightSum(Integer weightSum) {
		this.weightSum = weightSum;
	}

	public List<AlarmEvent> getAlarmEvents() {
		return alarmEvents;
	}

	public void setAlarmEvents(List<AlarmEvent> alarmEvents) {
		this.alarmEvents = alarmEvents;
	}

	public AlertEventStatus getStatus() {
		return status;
	}

	public void setStatus(AlertEventStatus status) {
		this.status = status;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public LocalDateTime getStartSmsSent() {
		return startSmsSent;
	}

	public void setStartSmsSent(LocalDateTime startSmsSent) {
		this.startSmsSent = startSmsSent;
	}

	public LocalDateTime getEndSmsSent() {
		return endSmsSent;
	}

	public void setEndSmsSent(LocalDateTime endSmsSent) {
		this.endSmsSent = endSmsSent;
	}

	public LocalDateTime getStartMailSent() {
		return startMailSent;
	}

	public void setStartMailSent(LocalDateTime startMailSent) {
		this.startMailSent = startMailSent;
	}

	public LocalDateTime getEndMailSent() {
		return endMailSent;
	}

	public void setEndMailSent(LocalDateTime endMailSent) {
		this.endMailSent = endMailSent;
	}

	public LocalDateTime getAstreinteSent() {
		return astreinteSent;
	}
	
	public void setAstreinteSent(LocalDateTime astreinteSent) {
		this.astreinteSent = astreinteSent;
	}

	@Override
	public String toString() {
		return "AlertEvent [id=" + id + ", status=" + status + ", alert=" + alert + ", alarmEvents="
				+ alarmEvents + ", start=" + start + ", lastUpdate=" + lastUpdate + ", weightSum=" + weightSum
				+ ", startSmsSent=" + startSmsSent + ", endSmsSent=" + endSmsSent + ", startMailSent=" + startMailSent
				+ ", endMailSent=" + endMailSent + "]";
	}

}
