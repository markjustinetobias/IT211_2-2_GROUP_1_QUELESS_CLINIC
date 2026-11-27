import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueManager {
    private static final int MAX_QUEUE_SIZE = 10;

    private final List<QueueTicket> queueList = new ArrayList<>();
    private final List<QueueTicket> allTickets = new ArrayList<>();   // for lookups and the report

    public QueueManager() { }

    public List<QueueTicket> getQueueList() { return Collections.unmodifiableList(queueList); }

    // Ordinary patient: FIFO (added to the end).
    public QueueTicket manageQueue(Patient patient) {
        QueueTicket t = new QueueTicket(patient, false);
        queueList.add(t);
        allTickets.add(t);
        return t;
    }

    // Staff-approved priority patient: ahead of ordinary patients, behind earlier priority ones.
    public QueueTicket manageQueue(Patient patient, PriorityRequest request) {
        if (!"Approved".equals(request.getStatus())) return manageQueue(patient);
        QueueTicket t = new QueueTicket(patient, true);
        int index = 0;
        while (index < queueList.size() && queueList.get(index).isPriority()) index++;
        queueList.add(index, t);
        allTickets.add(t);
        return t;
    }

    public boolean checkAvailability() { return queueList.size() < MAX_QUEUE_SIZE; }

    // Only one patient can be in consultation at a time.
    public QueueTicket processNext() {
        if (queueList.isEmpty() || findActive() != null) return null;
        QueueTicket t = queueList.remove(0);
        t.updateStatus("Called");
        return t;
    }

    public void updateStatus(QueueTicket ticket, String status) { ticket.updateStatus(status); }

    // Only patients still waiting can be cancelled or marked no-show.
    public boolean markCancelled(QueueTicket ticket) { return leaveQueue(ticket, "Cancelled"); }
    public boolean markNoShow(QueueTicket ticket) { return leaveQueue(ticket, "No-show"); }

    private boolean leaveQueue(QueueTicket ticket, String newStatus) {
        if (!queueList.remove(ticket)) return false;
        ticket.updateStatus(newStatus);
        return true;
    }

    public QueueTicket findByPatientID(String patientID) {
        for (QueueTicket t : allTickets) {
            if (t.getPatient().getPatientID().equalsIgnoreCase(patientID)) return t;
        }
        return null;
    }

    public QueueTicket findActive() {
        for (QueueTicket t : allTickets) {
            if (t.getStatus().equals("Called") || t.getStatus().equals("In Consultation")) return t;
        }
        return null;
    }

    public int positionOf(QueueTicket ticket) { return queueList.indexOf(ticket) + 1; }
    public int waitingCount() { return queueList.size(); }
    public int totalRegistered() { return allTickets.size(); }

    public int countByStatus(String status) {
        int n = 0;
        for (QueueTicket t : allTickets) if (t.getStatus().equals(status)) n++;
        return n;
    }
}
