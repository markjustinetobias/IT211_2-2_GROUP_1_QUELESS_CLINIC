public class ClinicStaff {
    private String staffID;
    private String name;

    public ClinicStaff(String staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public String getStaffID() { return staffID; }
    public String getName() { return name; }

    public QueueTicket callPatient(QueueManager manager) {
        return manager.processNext();
    }

    public void updateConsultation(QueueTicket ticket, String status) {
        ticket.updateStatus(status);
    }

    public void approvePriority(PriorityRequest request) {
        request.approveRequest();
    }
}
