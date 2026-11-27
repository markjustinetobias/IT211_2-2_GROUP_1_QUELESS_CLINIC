import java.time.LocalDate;

public class SessionReport {
    private final String reportID;
    private final LocalDate date = LocalDate.now();
    private final QueueManager manager;
    private final WaitingRoomMonitor monitor;

    public SessionReport(String reportID, QueueManager manager, WaitingRoomMonitor monitor) {
        this.reportID = reportID;
        this.manager = manager;
        this.monitor = monitor;
    }

    public String getReportID() { return reportID; }
    public LocalDate getDate() { return date; }

    public String generateReport() {
        return "=== Session Report " + reportID + " (" + date + ") ===\n"
             + "Total registered patients : " + manager.totalRegistered() + "\n"
             + "Completed consultations   : " + manager.countByStatus("Completed") + "\n"
             + "Cancellations             : " + manager.countByStatus("Cancelled") + "\n"
             + "No-shows                  : " + manager.countByStatus("No-show") + "\n"
             + "Waiting-room occupancy    : " + monitor.getCurrentOccupancy() + "/" + monitor.getCapacity();
    }

    public void displayReport() { System.out.println(generateReport()); }
}
