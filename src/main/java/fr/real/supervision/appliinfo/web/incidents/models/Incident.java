package fr.real.supervision.appliinfo.web.incidents.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "INCIDENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Incident implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "INCIDENT_NUMBER", nullable = false)
    private Integer incidentNumber;
    @Column(name = "INCIDENT_OBJET", length = 200)
    private String incidentObjet;
    @Column(name = "INCIDENT_DESTINATION")
    private String incidentDestination;
    @Column(name = "INCIDENT_GRAPHICAL_CHARTER")
    private String incidentGraphicalCharter;
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    @Column(name = "INCIDENT_NATURE", length = 250)
    private String incidentNature;
    @Column(name = "INCIDENT_APPLICATION", length = 250)
    private String incidentApplications;
    @Column(name = "INCIDENT_ENVIRONMENT", length = 100)
    private String incidentEnvironment;
    @Column(name = "INCIDENT_IMPACT_TYPE")
    private String incidentImpactType;
    @Column(name = "INCIDENT_IMPACT_DESCRIPTION", length = 250)
    private String incidentImpactDescription;
    @Column(name = "INCIDENT_START", length = 80)
    private String incidentStart;
    @Column(name = "INCIDENT_END", length = 80)
    private String incidentEnd;
    @Column(name = "INCIDENT_COMPLEMENTARY_INFOS", length = 200)
    private String incidentComplementaryInfos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getIncidentNumber() {
        return incidentNumber;
    }

    public void setIncidentNumber(Integer incidentNumber) {
        this.incidentNumber = incidentNumber;
    }

    public String getIncidentObjet() {
        return incidentObjet;
    }

    public void setIncidentObjet(String incidentObjet) {
        this.incidentObjet = incidentObjet;
    }

    public String getIncidentDestination() {
        return incidentDestination;
    }

    public void setIncidentDestination(String incidentDestination) {
        this.incidentDestination = incidentDestination;
    }

    public String getIncidentGraphicalCharter() {
        return incidentGraphicalCharter;
    }

    public void setIncidentGraphicalCharter(String incidentGraphicalCharter) {
        this.incidentGraphicalCharter = incidentGraphicalCharter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncidentNature() {
        return incidentNature;
    }

    public void setIncidentNature(String incidentNature) {
        this.incidentNature = incidentNature;
    }

    public String getIncidentApplications() {
        return incidentApplications;
    }

    public void setIncidentApplications(String incidentApplications) {
        this.incidentApplications = incidentApplications;
    }

    public String getIncidentEnvironment() {
        return incidentEnvironment;
    }

    public void setIncidentEnvironment(String incidentEnvironment) {
        this.incidentEnvironment = incidentEnvironment;
    }

    public String getIncidentImpactType() {
        return incidentImpactType;
    }

    public void setIncidentImpactType(String incidentImpactType) {
        this.incidentImpactType = incidentImpactType;
    }

    public String getIncidentImpactDescription() {
        return incidentImpactDescription;
    }

    public void setIncidentImpactDescription(String incidentImpactDescription) {
        this.incidentImpactDescription = incidentImpactDescription;
    }

    public String getIncidentStart() {
        return incidentStart;
    }

    public void setIncidentStart(String incidentStart) {
        this.incidentStart = incidentStart;
    }

    public String getIncidentEnd() {
        return incidentEnd;
    }

    public void setIncidentEnd(String incidentEnd) {
        this.incidentEnd = incidentEnd;
    }

    public String getIncidentComplementaryInfos() {
        return incidentComplementaryInfos;
    }

    public void setIncidentComplementaryInfos(String incidentComplementaryInfos) {
        this.incidentComplementaryInfos = incidentComplementaryInfos;
    }
}

