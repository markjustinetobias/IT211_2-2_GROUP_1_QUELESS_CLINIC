import java.time.LocalDateTime;

public class QueueTicket {
    private static int lastNumber = 0;      // used only to generate unique numbers

    private int ticketNumber;
    private String status;
    private LocalDateTime queueTime;
    private Patient patient;                // Patient 1 --- 0..* QueueTicket

    public QueueTicket(Patient patient) {
        this.ticketNumber = ++lastNumber;
        this.status = "Waiting";
        this.queueTime = LocalDateTime.now();
        this.patient = patient;
    }

    public int getTicketNumber() { return ticketNumber; }
    public String getStatus() { return status; }
    public LocalDateTime getQueueTime() { return queueTime; }
    public Patient getPatient() { return patient; }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public int getPosition(QueueManager manager) {
        return manager.getQueueList().indexOf(this) + 1;
    }
}
