package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class AlarmEvent implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "ALARM_EVENT_SEQ_GENERATOR")
	@SequenceGenerator(name = "ALARM_EVENT_SEQ_GENERATOR", sequenceName = "ALARM_EVENT_SEQ", allocationSize = 1)
	private Long id;

	@ManyToOne
	private Alarm alarm;

	@Column(name="BEGIN")
	private LocalDateTime start;

	/**
	 * Copy constructor
	 *
	 * @param alarmEvent
	 */
	public AlarmEvent(AlarmEvent alarmEvent) {
		alarm = alarmEvent.getAlarm();
		start = alarmEvent.getStart();
	}

	public AlarmEvent() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "AlarmEvent [id=" + this.id + ", start=" + this.start + ", alarmName=" + this.alarm.getName() + ", alarmWeight=" + this.alarm.getWeight() + "]";
	}
}