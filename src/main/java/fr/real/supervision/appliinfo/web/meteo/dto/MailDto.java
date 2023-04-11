package fr.real.supervision.appliinfo.web.meteo.dto;

public class MailDto {

    private String subjectMail;

    private String to;


    private String From;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getSubjectMail() {
        return subjectMail;
    }

    public void setSubjectMail(String subjectMail) {
        this.subjectMail = subjectMail;
    }
}
