package fr.real.supervision.appliinfo.web.profiles.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ProfileDto {

    private Long id;

    @NotBlank(message = "la nom est obligatoire")
    private String name;

    private String description;

    private Boolean hidden = false;

    private List<ProfileFunctionalityDto> profileFunctionalitiesDto;

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

    public void setProfileFunctionalitiesDto(List<ProfileFunctionalityDto> profileFunctionalitiesDto) {
        this.profileFunctionalitiesDto = profileFunctionalitiesDto;
    }

    public List<ProfileFunctionalityDto> getProfileFunctionalitiesDto() {
        return profileFunctionalitiesDto;
    }

}
