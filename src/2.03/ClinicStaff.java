public class ClinicStaff {
    private final String staffID;
    private final String name;

    public ClinicStaff(String staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public String getStaffID() { return staffID; }
    public String getName() { return name; }

    public QueueTicket callPatient(QueueManager manager) {
        return manager.processNext();
    }

    public void updateConsultation(QueueManager manager, QueueTicket ticket, String status) {
        manager.updateStatus(ticket, status);
    }

    // Called -> In Consultation -> Completed. Returns the new status, or null if nothing to advance.
    public String advanceConsultation(QueueManager manager, QueueTicket ticket) {
        String next;
        if ("Called".equals(ticket.getStatus())) next = "In Consultation";
        else if ("In Consultation".equals(ticket.getStatus())) next = "Completed";
        else return null;
        manager.updateStatus(ticket, next);
        return next;
    }

    public boolean markNoShow(QueueManager manager, QueueTicket ticket) {
        return manager.markNoShow(ticket);
    }

    public boolean approvePriority(PriorityRequest request) {
        // Clinic rule: only requests with a stated reason can be approved.
        if (request.getReason() != null && !request.getReason().isBlank()) {
            request.approveRequest(this);
            return true;
        }
        request.rejectRequest(this);
        return false;
    }
}
