package fr.real.supervision.appliinfo.web.holidays.dto;

import javax.validation.constraints.NotBlank;

public class HolidayDto {

    private Long id;

    @NotBlank(message = "la date est obligatoire")
    private String day;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

}
