public class PriorityRequest {
    private final String requestID;
    private final String reason;
    private String status;
    private final Patient patient;        // association: who is asking
    private ClinicStaff approvedBy;       // staff who decided the request

    public PriorityRequest(String requestID, Patient patient, String reason) {
        if (requestID == null || requestID.isBlank() || patient == null) {
            throw new IllegalArgumentException("Request ID and patient are required.");
        }
        this.requestID = requestID;
        this.patient = patient;
        this.reason = reason;
        this.status = "Pending";
    }

    public String getRequestID() { return requestID; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public Patient getPatient() { return patient; }
    public ClinicStaff getApprovedBy() { return approvedBy; }

    public void approveRequest(ClinicStaff staff) {
        this.status = "Approved";
        this.approvedBy = staff;
    }

    public void rejectRequest(ClinicStaff staff) {
        this.status = "Rejected";
        this.approvedBy = staff;
    }
}
