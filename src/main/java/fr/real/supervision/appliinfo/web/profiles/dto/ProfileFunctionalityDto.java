package fr.real.supervision.appliinfo.web.profiles.dto;

public class ProfileFunctionalityDto {

    private Long id;

    private Long profileDtoId;

    private Long functionalityDtoId;

    private Boolean read;

    private Boolean write;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileDtoId() {
        return profileDtoId;
    }

    public void setProfileDtoId(Long profileDtoId) {
        this.profileDtoId = profileDtoId;
    }

    public Long getFunctionalityDtoId() {
        return functionalityDtoId;
    }

    public void setFunctionalityDtoId(Long functionalityDtoId) {
        this.functionalityDtoId = functionalityDtoId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getWrite() {
        return write;
    }

    public void setWrite(Boolean write) {
        this.write = write;
    }
}
