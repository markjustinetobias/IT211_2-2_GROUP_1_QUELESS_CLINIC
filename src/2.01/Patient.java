public class Patient {
    private String name;
    private String patientID;

    public Patient(String name, String patientID) {
        this.name = name;
        this.patientID = patientID;
    }

    public String getName() { return name; }
    public String getPatientID() { return patientID; }

    public QueueTicket requestQueue(QueueManager manager) {
        return manager.manageQueue(this);
    }

    public String checkStatus(QueueTicket ticket) {
        return ticket.getStatus();
    }
}
