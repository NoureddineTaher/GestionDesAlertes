package fr.real.supervision.appliinfo.connector.itm;

import javax.xml.bind.annotation.XmlAttribute;

public class ItmAlarm {

	/**
	 * Heure de déclenchement de l'alarme (HGBLTMSTMP)
	 */

	private String start;

	/**
	 * Nom de l'alarme/situation ITM tronquée à 32 caractères (HSITNAME)
	 * nomenclature : (REAL, Linux, cloud)_Nom appli concernée_type de test(exploit,
	 * ...)_severite(critical, ...)
	 */
	private String name;

	/**
	 * Status de l'alarme (HDELTASTAT) :
	 *
	 * <ul>
	 * <li>y : ouverte</li>
	 * <li>a : acquittée</li>
	 * <li>e : réouverte</li>
	 * </ul>
	 */

	private String status;

	/**
	 * Nom de la passerelle ITM qui remonte l'alarme (HNODE)
	 */
	private String passerelle;

	/**
	 * Serveur qui a generé l'alarme (HORIGINNODE)
	 *
	 * Template : [instance de l'agent:]serveur:numero agent de surveillance
	 */
	private String server;

	private String severite;
	private String displayItem;


	@XmlAttribute(name = "HGBLTMSTMP")
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@XmlAttribute(name = "HSITNAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "HDELTASTAT")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlAttribute(name = "HNODE")
	public String getPasserelle() {
		return passerelle;
	}

	public void setPasserelle(String passerelle) {
		this.passerelle = passerelle;
	}

	@XmlAttribute(name = "HORIGINNODE")
	public String getServer() {
		return server;
	}

	@XmlAttribute(name = "ATOMIZE")
	public String getDisplayItem(){
		return displayItem;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setDisplayItem(String displayItem) {
		this.displayItem = displayItem;
	}

	public String getSeverite() {
		return severite;
	}

	public void setSeverite(String severite) {
		this.severite = severite;
	}

	public ItmAlarm(String start, String name, String status, String passerelle, String server, String severite, String displayItem) {
		this.start = start;
		this.name = name;
		this.status = status;
		this.passerelle = passerelle;
		this.server = server;
		this.severite = severite;
		this.displayItem = displayItem;
	}

	public ItmAlarm() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItmAlarm [\n\tstart=");
		builder.append(start);
		builder.append("\n\t, name=");
		builder.append(name);
		builder.append("\n\t, status=");
		builder.append(status);
		builder.append("\n\t, passerelle=");
		builder.append(passerelle);
		builder.append("\n\t, server=");
		builder.append(server);
		builder.append("\n\t, severite=");
		builder.append(severite);
		builder.append("\n]");
		return builder.toString();
	}

}