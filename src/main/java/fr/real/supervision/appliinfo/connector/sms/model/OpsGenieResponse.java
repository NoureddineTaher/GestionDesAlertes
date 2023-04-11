package fr.real.supervision.appliinfo.connector.sms.model;

public class OpsGenieResponse {

    private String result;
    private Double took;
    private String requestId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
