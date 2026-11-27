import java.time.LocalDate;

public class SessionReport {
    private String reportID;
    private LocalDate date;

    public SessionReport(String reportID) {
        this.reportID = reportID;
        this.date = LocalDate.now();
    }

    public String getReportID() { return reportID; }
    public LocalDate getDate() { return date; }

    public String generateReport(QueueManager manager, WaitingRoomMonitor monitor) {
        return "Report " + reportID + " (" + date + ")\n"
             + "Patients still waiting: " + manager.getQueueList().size() + "\n"
             + "Waiting-room occupancy: " + monitor.getCurrentOccupancy()
             + "/" + monitor.getCapacity();
    }

    public void displayReport(QueueManager manager, WaitingRoomMonitor monitor) {
        System.out.println(generateReport(manager, monitor));
    }
}
