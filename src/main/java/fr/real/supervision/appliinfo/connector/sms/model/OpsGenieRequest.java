package fr.real.supervision.appliinfo.connector.sms.model;

public class OpsGenieRequest {

    private String message;

    public OpsGenieRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }@Override
    public String toString() {
        return "OpsGenieRequest [message=" + message + "]";
    }

}
