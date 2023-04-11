package fr.real.supervision.appliinfo.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WorkingHour {

	private static final DateTimeFormatter HORAIRE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private DayOfWeek day;

	private LocalTime minHour;

	private LocalTime maxHour;

	public WorkingHour(DayOfWeek day, LocalTime minHour, LocalTime maxHour) {
		this.day = day;
		this.minHour = minHour;
		this.maxHour = maxHour;
	}

	public WorkingHour() {
		super();
	}

	/**
	 * 
	 * @param workingHour heures de travail sous la forme :
	 *                    jour(heure_debut-heurefin) exemple : 1(08:00-12:00) pour
	 *                    le lundi de 8h à 12h
	 */
	public WorkingHour(String workingHour) {

		int indexParentheseOuvrante = workingHour.indexOf('(');
		int indexParentheseFermante = workingHour.indexOf(')');
		int indexSeparateurDebutFin = workingHour.indexOf('-');

		String dayValue = workingHour.substring(0, indexParentheseOuvrante);
		String begin = workingHour.substring(indexParentheseOuvrante + 1, indexSeparateurDebutFin);
		String end = workingHour.substring(indexSeparateurDebutFin + 1, indexParentheseFermante);

		this.day = DayOfWeek.of(Integer.valueOf(dayValue));
		this.minHour = LocalTime.parse(begin, HORAIRE_FORMATTER);
		this.maxHour = LocalTime.parse(end, HORAIRE_FORMATTER);

	}

	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}

	public LocalTime getMinHour() {
		return minHour;
	}

	public void setMinHour(LocalTime minHour) {
		this.minHour = minHour;
	}

	public LocalTime getMaxHour() {
		return maxHour;
	}

	public void setMaxHour(LocalTime maxHour) {
		this.maxHour = maxHour;
	}

	public String getMaxHourFormated() {
		return this.maxHour.format(HORAIRE_FORMATTER);
	}

	public String getMinHourFormated() {
		return this.minHour.format(HORAIRE_FORMATTER);
	}

	public boolean contains(LocalDateTime d) {
		DayOfWeek theDay = d.getDayOfWeek();
		LocalTime theHour = d.toLocalTime();
		
		return (theDay.equals(day) && theHour.isAfter(minHour)
				&& theHour.isBefore(maxHour));
	}

	public boolean isSameDay(LocalDateTime d) {
		DayOfWeek theDay = d.getDayOfWeek();

		return theDay.equals(day);
	}

	@Override
	public String toString() {
		return day.getValue() + "(" + this.minHour.format(HORAIRE_FORMATTER) + "-"
				+ this.maxHour.format(HORAIRE_FORMATTER) + ")";
	}

}
