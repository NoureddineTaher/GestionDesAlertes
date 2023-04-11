package fr.real.supervision.appliinfo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "HOLIDAY")
public class Holiday extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "HOLIDAY_SEQ_GENERATOR")
    @SequenceGenerator(name = "HOLIDAY_SEQ_GENERATOR", sequenceName = "HOLIDAY_SEQ", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDay() {
        return day;
    }

    public String displayDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return day.format(formatter);
    }

}
