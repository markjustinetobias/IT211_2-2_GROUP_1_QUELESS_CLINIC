public class Patient {
    private final String name;
    private final String patientID;
    private QueueTicket ticket;           // set when the patient joins the queue

    public Patient(String name, String patientID) {
        if (name == null || name.isBlank() || patientID == null || patientID.isBlank()) {
            throw new IllegalArgumentException("Name and patient ID are required.");
        }
        this.name = name;
        this.patientID = patientID;
    }

    public String getName() { return name; }
    public String getPatientID() { return patientID; }
    public QueueTicket getTicket() { return ticket; }

    public QueueTicket requestQueue(QueueManager manager) {
        ticket = manager.manageQueue(this);
        return ticket;
    }

    public String checkStatus() {
        return ticket == null ? "Not in queue" : ticket.getStatus();
    }
}
