package fr.real.supervision.appliinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //On n'a pas activé le jpaAuditing à cause de besoin
    //On set manuellement les valeurs dans chaque méthode

    @Column(name = "created_by", nullable = false, length = 64,updatable = false)
    @JsonIgnore
    private String createdBy;

    @Column(name = "created_date",updatable = false)
    @JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "last_modified_by", length = 64)
    @JsonIgnore
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    @JsonIgnore
    private LocalDateTime lastModifiedDate ;

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
