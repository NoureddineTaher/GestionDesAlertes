package fr.real.supervision.appliinfo.web.communication.dto;

import fr.real.supervision.appliinfo.model.DiffusionGroup;

import java.util.List;

public class CommunicationDto {

    private Boolean sendSMS;

    private Boolean sendMail;

    private String smsContent;

    private String emailObject;

    private String emailContent;

    private List<DiffusionGroup> diffusionGroups;

    public Boolean getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public Boolean getSendMail() {
        return sendMail;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getEmailObject() {
        return emailObject;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public List<DiffusionGroup> getDiffusionGroups() {
        return diffusionGroups;
    }

    public void setDiffusionGroups(List<DiffusionGroup> diffusionGroups) {
        this.diffusionGroups = diffusionGroups;
    }
}
