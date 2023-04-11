package fr.real.supervision.appliinfo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "PROFILE")
public class Profile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "PROFILE_SEQ_GENERATOR")
    @SequenceGenerator(name = "PROFILE_SEQ_GENERATOR", sequenceName = "PROFILE_SEQ", allocationSize = 1)
    private Long id;

    @Column(unique = true, length = 15)
    @Size(max = 15, message = "test")
    private String name;

    @Column(length = 50)
    private String description;

    private Boolean hidden = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "profile")
    List<ProfileFunctionality> profileFunctionalities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public List<ProfileFunctionality> getProfileFunctionalities() {
        return profileFunctionalities;
    }

    public void setProfileFunctionalities(List<ProfileFunctionality> profileFunctionalities) {
        this.profileFunctionalities = profileFunctionalities;
    }
}
