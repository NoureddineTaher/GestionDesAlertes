package fr.real.supervision.appliinfo.web.maintenance.dto;

import fr.real.supervision.appliinfo.model.Alert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class MaintenanceDto {

    private Long id;

    @NotEmpty(message = "Veuillez spécifier une date de début")
    private String startTime;

    @NotEmpty(message = "Veuillez spécifier une date de fin")
    private String endTime;

    private String comments;

    private Alert alert;

    private String lastModifiedDate;

    private String lastModifiedBy;

    @Size(min = 1, message = "Veuillez sélectionner au moins une alerte")
    private List<Alert> alerts;

    public boolean validate(){
        //EndTime has to be after startTime - comparison is working with stringDate yyyy-MM-ddThh:mm
        return startTime.compareTo(endTime) < 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}
