package fr.real.supervision.appliinfo.web.incidents.service;

import fr.real.supervision.appliinfo.web.incidents.dto.IncidentDto;
import fr.real.supervision.appliinfo.web.incidents.models.Incident;
import fr.real.supervision.appliinfo.web.incidents.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {
    private final IncidentRepository incidentRepository;
    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }
    public Incident save(IncidentDto incidentDto) {
        Integer incidentNumber = incidentDto.getIncidentNumber();
        Optional<Incident> lastIncident= incidentRepository.findById((long)incidentNumber);
        if(lastIncident.isPresent()){
            incidentRepository.deleteById((long)incidentNumber);
        }
        Incident incident = new Incident();
        incident.setId(incidentNumber);
        incident.setIncidentNumber(incidentNumber);
        incident.setIncidentObjet(incidentDto.getIncidentObjet());
        incident.setIncidentDestination(incidentDto.getIncidentDestination());
        incident.setIncidentImpactDescription(incidentDto.getIncidentImpactDescription());
        incident.setIncidentGraphicalCharter(incidentDto.getIncidentGraphicalCharter());
        incident.setDescription(incidentDto.getDescription());
        incident.setIncidentNature(incidentDto.getIncidentNature());
        incident.setIncidentApplications(incidentDto.getIncidentApplications());
        incident.setIncidentEnvironment(incidentDto.getIncidentEnvironment());
        incident.setIncidentImpactType(incidentDto.getIncidentImpactType());
        incident.setIncidentStart(incidentDto.getIncidentStart());
        incident.setIncidentEnd(incidentDto.getIncidentEnd());
        incident.setIncidentComplementaryInfos(incidentDto.getIncidentComplementaryInfos());
        incident.setIncidentApplications(incidentDto.getIncidentApplications());
        incident.setDescription(incidentDto.getDescription());
        return  incidentRepository.save(incident);
    }
    public Optional<Incident> getIncidentById(long id) {
        return incidentRepository.findById(id);
    }

    public Incident getLastIncident() {
        return incidentRepository.findFirstByOrderByIdDesc();
    }

    public void deleteIncidentCloture(Incident incident){
        if(incident.getIncidentGraphicalCharter().equals("Clôture d'incident")){
            incidentRepository.deleteById(incident.getId());
        }
    }

}