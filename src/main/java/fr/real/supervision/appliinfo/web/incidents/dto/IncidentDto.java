package fr.real.supervision.appliinfo.web.incidents.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class IncidentDto {

    private Long id;
    private Integer incidentNumber;
    private String incidentObjet;
    private String incidentDestination;
    private String incidentImpactDescription;
    private String incidentGraphicalCharter;
    private String description;
    private String incidentNature;
    private String incidentApplications;
    private String incidentEnvironment;
    private String incidentImpactType;
    private String incidentStart;
    private String incidentEnd;
    private String incidentComplementaryInfos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getIncidentImpactDescription() {
        return incidentImpactDescription;
    }

    public void setIncidentImpactDescription(String incidentImpactDescription) {
        this.incidentImpactDescription = incidentImpactDescription;
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