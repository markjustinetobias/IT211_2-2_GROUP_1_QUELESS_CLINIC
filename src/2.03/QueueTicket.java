import java.time.LocalDateTime;
import java.util.List;

public class QueueTicket {
    private static final List<String> VALID_STATUSES =
            List.of("Waiting", "Called", "In Consultation", "Completed", "Cancelled", "No-show");
    private static int lastTicketNumber = 0;

    private final int ticketNumber;
    private String status;
    private final LocalDateTime queueTime;
    private final Patient patient;        // association: Patient 1 - 0..* QueueTicket
    private final boolean priority;       // stores priority status when applicable

    public QueueTicket(Patient patient, boolean priority) {
        if (patient == null) throw new IllegalArgumentException("A ticket needs a patient.");
        this.ticketNumber = ++lastTicketNumber;
        this.status = "Waiting";
        this.queueTime = LocalDateTime.now();
        this.patient = patient;
        this.priority = priority;
    }

    public int getTicketNumber() { return ticketNumber; }
    public String getStatus() { return status; }
    public LocalDateTime getQueueTime() { return queueTime; }
    public Patient getPatient() { return patient; }
    public boolean isPriority() { return priority; }

    public void updateStatus(String newStatus) {
        if (!VALID_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Unknown status: " + newStatus);
        }
        this.status = newStatus;
    }

    public int getPosition(QueueManager manager) { return manager.positionOf(this); }
}
