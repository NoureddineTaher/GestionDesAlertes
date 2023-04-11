package fr.real.supervision.appliinfo.web.incidents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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

}