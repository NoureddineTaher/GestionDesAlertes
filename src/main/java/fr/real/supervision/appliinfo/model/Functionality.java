package fr.real.supervision.appliinfo.model;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "FUNCTIONALITY")
public class Functionality {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "FUNCTIONALITY_SEQ_GENERATOR")
    @SequenceGenerator(name = "FUNCTIONALITY_SEQ_GENERATOR", sequenceName = "FUNCTIONALITY_SEQ", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FunctionalityType type;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "functionality")
    Set<ProfileFunctionality> profileFunctionalities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FunctionalityType getType() {
        return type;
    }

    public void setType(FunctionalityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProfileFunctionality> getProfileFunctionalities() {
        return profileFunctionalities;
    }

    public void setProfileFunctionalities(Set<ProfileFunctionality> profileFunctionalities) {
        this.profileFunctionalities = profileFunctionalities;
    }
}
