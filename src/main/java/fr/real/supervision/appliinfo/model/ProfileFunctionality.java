package fr.real.supervision.appliinfo.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "PROFILE_FUNCTIONALITY")
public class ProfileFunctionality {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "PROFILE_FUNCTIONALITY_SEQ_GENERATOR")
    @SequenceGenerator(name = "PROFILE_FUNCTIONALITY_SEQ_GENERATOR", sequenceName = "PROFILE_FUNCTIONALITY_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionality_id")
    private Functionality functionality;

    private boolean read;

    private boolean write;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Functionality getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }
}
