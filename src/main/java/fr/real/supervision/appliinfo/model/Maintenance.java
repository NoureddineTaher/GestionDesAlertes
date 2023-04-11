package fr.real.supervision.appliinfo.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "MAINTENANCE")
public class Maintenance extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "MAINTENANCE_SEQ_GENERATOR")
    @SequenceGenerator(name = "MAINTENANCE_SEQ_GENERATOR", sequenceName = "MAINTENANCE_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "start_time")
    private java.time.LocalDateTime startTime;

    @Column(name = "end_time")
    private java.time.LocalDateTime endTime;

    @Column
    private String comments;

    @ManyToOne
    private Alert alert;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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

    @Override
    public String toString() {
        return "Maintenance [" +
                "debut =" + this.startTime +
                ", fin =" + this.endTime +
                ", commentaire ='" + this.comments + '\'' +
                ", alert =" + this.alert +
                ']';
    }
}
