package fr.real.supervision.appliinfo.connector.sms.model;

public class OpsGenieErrorResponse {

        private String message;
        private Double took;
        private String requestId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getTook() {
        return took;
    }

    public void setTook(Double took) {
        this.took = took;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
