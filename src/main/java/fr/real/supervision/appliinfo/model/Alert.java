package fr.real.supervision.appliinfo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ALERT")
public class Alert extends AbstractAuditingEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "ALERT_SEQ_GENERATOR")
	@SequenceGenerator(name = "ALERT_SEQ_GENERATOR", sequenceName = "ALERT_SEQ", allocationSize = 1)
	private Long id;

	@Column(unique = true)
	private String name;

	@Column(length = 512)
	private String description;

	@Column
	private boolean active;

	/**
	 * Les heures pendant lesquelles on peut envoyer un sms. exemple :
	 * 10:30-12:00;14:00-17:00
	 */
	@Column
	private String hoursWhenSmsIsAllowed;

	@Column
	private String hoursWhenMailIsAllowed;

	@Column
	private String hoursWhenAstreinteIsAllowed;

	@ManyToOne
	private Category category;

	@ManyToMany
	private Set<Alarm> alarms;

	@ManyToMany
	private Set<DiffusionGroup> diffusionGroups;

	@Column
	private String startSms;

	@Column
	private String endSms;

	@Column
	private String startSubject;

	@Column
	private String startBody;

	@Column
	private String endSubject;

	@Column
	private String endBody;

	@Column
	private String astreinte;

	public String getStartSms() {
		return startSms;
	}

	public void setStartSms(String startSms) {
		this.startSms = startSms;
	}

	public String getEndSms() {
		return endSms;
	}

	public void setEndSms(String endSms) {
		this.endSms = endSms;
	}

	public String getStartSubject() {
		return startSubject;
	}

	public void setStartSubject(String startSubject) {
		this.startSubject = startSubject;
	}

	public String getStartBody() {
		return startBody;
	}

	public void setStartBody(String startBody) {
		this.startBody = startBody;
	}

	public String getEndSubject() {
		return endSubject;
	}

	public void setEndSubject(String endSubject) {
		this.endSubject = endSubject;
	}

	public String getEndBody() {
		return endBody;
	}

	public void setEndBody(String endBody) {
		this.endBody = endBody;
	}

	public String getAstreinte() {
		return astreinte;
	}

	public void setAstreinte(String astreinte) {
		this.astreinte = astreinte;
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

	public Long getId() {
		return id;
	}

	public void setHoursWhenSmsIsAllowed(List<WorkingHour> hoursWhenMailIsAllowed) {
		this.hoursWhenSmsIsAllowed = hoursWhenMailIsAllowed.stream().map(WorkingHour::toString).reduce("",
				(a, b) -> a.isEmpty() ? b : a + ";" + b);
	}

	public void setHoursWhenSmsIsAllowed(String hoursWhenSmsIsAllowed) {
		this.hoursWhenSmsIsAllowed = hoursWhenSmsIsAllowed;
	}

	public List<WorkingHour> getHoursWhenMailIsAllowed() {
		return stringToListWorkingHour(hoursWhenMailIsAllowed);
	}

	public List<WorkingHour> getHoursWhenSmsIsAllowedList() {
		return stringToListWorkingHour(hoursWhenSmsIsAllowed);
	}

	public List<WorkingHour> getHoursWhenAstreinteIsAllowedList() {
		return stringToListWorkingHour(hoursWhenAstreinteIsAllowed);
	}

	public void setHoursWhenMailIsAllowed(List<WorkingHour> hoursWhenMailIsAllowed) {
		this.hoursWhenMailIsAllowed = hoursWhenMailIsAllowed.stream().map(WorkingHour::toString).reduce("",
				(a, b) -> a.isEmpty() ? b : a + ";" + b);
	}

	public void setHoursWhenMailIsAllowed(String hoursWhenMailIsAllowed) {
		this.hoursWhenMailIsAllowed = hoursWhenMailIsAllowed;
	}

	public String getHoursWhenAstreinteIsAllowed() {
		return hoursWhenAstreinteIsAllowed;
	}

	public void setHoursWhenAstreinteIsAllowed(String hoursWhenAstreinteIsAllowed) {
		this.hoursWhenAstreinteIsAllowed = hoursWhenAstreinteIsAllowed;
	}

	public Set<Alarm> getAlarms() {
		return alarms;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setAlarms(Set<Alarm> alarms) {
		this.alarms = alarms;
	}

	public Set<DiffusionGroup> getDiffusionGroups() {
		return diffusionGroups;
	}

	public void setDiffusionGroups(Set<DiffusionGroup> diffusionGroups) {
		this.diffusionGroups = diffusionGroups;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private List<WorkingHour> stringToListWorkingHour(String s) {
		List<WorkingHour> l = null;
		if (s != null && !s.isEmpty()) {
			l = Stream.of(s.split("\\s*;\\s*")).map(WorkingHour::new).collect(Collectors.toList());
		} else {
			l = new ArrayList<>();
		}
		return l;
	}

	@Override
	public String toString() {
		return "Alert [" + "nom ='" + name + '\'' + ", description='" + description + '\'' + ", active=" + active
				+ ", smsHoraire ='" + hoursWhenSmsIsAllowed + '\'' + ", mailHoraire='" + hoursWhenMailIsAllowed + '\''
				+ ", astreinteHoraire ='" + hoursWhenAstreinteIsAllowed + '\'' + ", categorie =" + category
				+ ", alarmes=" + alarms + ", diffusion=" + diffusionGroups + ", debutSmsMessage ='" + startSms + '\''
				+ ", finSmsMessage='" + endSms + '\'' + ", startSubject='" + startSubject + '\'' + ", startBody='"
				+ startBody + '\'' + ", endSubject='" + endSubject + '\'' + ", endBody='" + endBody + '\''
				+ ", astreinte='" + astreinte + '\'' + ']';
	}
}
