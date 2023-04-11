package fr.real.supervision.appliinfo.web.alerts.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import fr.real.supervision.appliinfo.model.Alarm;
import fr.real.supervision.appliinfo.model.Category;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.model.WorkingHour;

public class AlertDto {

	private Long id;

	@NotBlank(message = "Le nom d'alerte est obligatoire")
	private String name;

	private String description;

	private boolean active;

	private List<WorkingHour> hoursWhenSmsIsAllowed;

	private List<WorkingHour> hoursWhenMailIsAllowed;

	private List<WorkingHour> hoursWhenAstreinteIsAllowed;

	private List<DiffusionGroup> diffusionGroups;

	private Set<Alarm> alarms;

	private Category category;

	private String startSms;

	private String endSms;

	private String startSubject;

	private String startBody;

	private String endSubject;

	private String endBody;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<WorkingHour> getHoursWhenSmsIsAllowed() {
		return hoursWhenSmsIsAllowed;
	}

	public String hoursWhenSmsIsAllowedString() {
		return workingHourToString(this.hoursWhenSmsIsAllowed);
	}

	public String hoursWhenMailIsAllowedString() {
		return workingHourToString(this.hoursWhenMailIsAllowed);
	}

	public String hoursWhenAstreinteIsAllowedString() {
		return workingHourToString(this.hoursWhenAstreinteIsAllowed);
	}

	private String workingHourToString(List<WorkingHour> l) {
		if (l != null) {
			return l.stream().filter(s -> s.getDay() != null || s.getMinHour() != null || s.getMaxHour() != null)
					.map(WorkingHour::toString).collect(Collectors.joining(";"));
		} else {
			return null;
		}
	}

	public void setHoursWhenSmsIsAllowed(List<WorkingHour> hoursWhenSmsIsAllowed) {
		this.hoursWhenSmsIsAllowed = hoursWhenSmsIsAllowed;
	}

	public List<WorkingHour> getHoursWhenMailIsAllowed() {
		return hoursWhenMailIsAllowed;
	}

	public void setHoursWhenMailIsAllowed(List<WorkingHour> hoursWhenMailIsAllowed) {
		this.hoursWhenMailIsAllowed = hoursWhenMailIsAllowed;
	}

	public List<WorkingHour> getHoursWhenAstreinteIsAllowed() {
		return hoursWhenAstreinteIsAllowed;
	}

	public void setHoursWhenAstreinteIsAllowed(List<WorkingHour> hoursWhenAstreinteIsAllowed) {
		this.hoursWhenAstreinteIsAllowed = hoursWhenAstreinteIsAllowed;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<DiffusionGroup> getDiffusionGroups() {
		return diffusionGroups;
	}

	public void setDiffusionGroups(List<DiffusionGroup> diffusionGroups) {
		this.diffusionGroups = diffusionGroups;
	}

	public Set<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(Set<Alarm> alarms) {
		this.alarms = alarms;
	}

}
