public class PriorityRequest {
    private String requestID;
    private String reason;
    private String status;

    public PriorityRequest(String requestID, String reason) {
        this.requestID = requestID;
        this.reason = reason;
        this.status = "Pending";
    }

    public String getRequestID() { return requestID; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }

    public void approveRequest() { this.status = "Approved"; }
    public void rejectRequest() { this.status = "Rejected"; }
}
