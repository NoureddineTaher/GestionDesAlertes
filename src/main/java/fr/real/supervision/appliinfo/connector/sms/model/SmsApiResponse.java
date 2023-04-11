package fr.real.supervision.appliinfo.connector.sms.model;

public class SmsApiResponse {

    private Integer status;
    private String message;
    private String ticket;
    private Integer cost;
    private Integer credits;
    private Integer total;
    private Integer sent;
    private Integer blacklisted;
    private Integer duplicated;
    private Integer invalid;
    private Integer npai;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }

    public Integer getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Integer blacklisted) {
        this.blacklisted = blacklisted;
    }

    public Integer getDuplicated() {
        return duplicated;
    }

    public void setDuplicated(Integer duplicated) {
        this.duplicated = duplicated;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    public Integer getNpai() {
        return npai;
    }

    public void setNpai(Integer npai) {
        this.npai = npai;
    }

}
