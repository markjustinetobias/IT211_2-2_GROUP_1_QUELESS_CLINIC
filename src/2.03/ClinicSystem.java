import java.util.Scanner;

// Console menu only: it asks for input, calls the other classes, and holds no clinic rules.
public class ClinicSystem {
    private final Scanner in = new Scanner(System.in);
    private final QueueManager queueManager = new QueueManager();
    private final WaitingRoomMonitor monitor = new WaitingRoomMonitor(5);
    private final ClinicStaff staff = new ClinicStaff("S-001", "Nurse Ana");
    private final InputValidator validator = new InputValidator();
    private final SessionReport report = new SessionReport("R-001", queueManager, monitor);
    private int patientCount = 0;

    public void displayMenu() {
        System.out.println("\n===== QueueLess Clinic System =====");
        System.out.println("1. Register patient (join queue)");
        System.out.println("2. Check queue position / status");
        System.out.println("3. Call next patient");
        System.out.println("4. Update consultation progress");
        System.out.println("5. Cancel a waiting patient");
        System.out.println("6. Mark a waiting patient as no-show");
        System.out.println("7. Monitor waiting room");
        System.out.println("8. View session summary");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = in.hasNextLine() ? in.nextLine() : "0";
            if (!validator.validateMenuChoice(choice, 0, 8)) {
                validator.showErrorMessage("enter a number from 0 to 8.");
                continue;
            }
            running = handleSelection(Integer.parseInt(choice.trim()));
        }
        System.out.println("Goodbye.");
    }

    public boolean handleSelection(int choice) {
        switch (choice) {
            case 1 -> registerPatient();
            case 2 -> checkStatus();
            case 3 -> callNext();
            case 4 -> updateConsultation();
            case 5 -> cancelPatient();
            case 6 -> markNoShow();
            case 7 -> checkWaitingRoom();
            case 8 -> report.displayReport();
            case 0 -> { return false; }
        }
        return true;
    }

    private void registerPatient() {
        if (!queueManager.checkAvailability()) {
            System.out.println("The queue is full. Please try again later.");
            return;
        }
        String name = ask("Patient name: ");
        if (!validator.validateInput(name)) {
            validator.showErrorMessage("name cannot be empty.");
            return;
        }
        Patient patient = new Patient(name.trim(), String.format("P-%03d", ++patientCount));
        String reason = ask("Priority reason (press Enter for none): ");

        QueueTicket ticket;
        if (validator.validateInput(reason)) {
            PriorityRequest request = new PriorityRequest("PR-" + patientCount, patient, reason.trim());
            staff.approvePriority(request);
            ticket = queueManager.manageQueue(patient, request);
            System.out.println("Priority request " + request.getStatus() + ".");
        } else {
            ticket = patient.requestQueue(queueManager);
        }
        monitor.patientArrived();
        System.out.println(patient.getName() + " (" + patient.getPatientID() + ") got ticket #"
                + ticket.getTicketNumber() + ", position " + ticket.getPosition(queueManager));
        if (monitor.monitorOccupancy()) monitor.alertStaff();
    }

    private void checkStatus() {
        QueueTicket ticket = askTicket();
        if (ticket == null) return;
        String text = "Ticket #" + ticket.getTicketNumber() + ": " + ticket.getStatus();
        if (ticket.getStatus().equals("Waiting")) text += ", position " + ticket.getPosition(queueManager);
        System.out.println(text);
    }

    private void callNext() {
        if (queueManager.waitingCount() == 0) {
            System.out.println("No patients are waiting.");
            return;
        }
        QueueTicket ticket = staff.callPatient(queueManager);
        if (ticket == null) {
            System.out.println("Finish the current consultation first.");
            return;
        }
        monitor.patientLeft();
        System.out.println("Calling ticket #" + ticket.getTicketNumber()
                + " (" + ticket.getPatient().getName() + ")");
    }

    private void updateConsultation() {
        QueueTicket ticket = queueManager.findActive();
        if (ticket == null) {
            System.out.println("No patient is currently called or in consultation.");
            return;
        }
        String status = staff.advanceConsultation(queueManager, ticket);
        System.out.println("Ticket #" + ticket.getTicketNumber() + " is now: " + status);
    }

    private void cancelPatient() {
        QueueTicket ticket = askTicket();
        if (ticket == null) return;
        if (queueManager.markCancelled(ticket)) {
            monitor.patientLeft();
            System.out.println("Ticket #" + ticket.getTicketNumber() + " cancelled.");
        } else {
            System.out.println("Only waiting patients can be cancelled.");
        }
    }

    private void markNoShow() {
        QueueTicket ticket = askTicket();
        if (ticket == null) return;
        if (staff.markNoShow(queueManager, ticket)) {
            monitor.patientLeft();
            System.out.println("Ticket #" + ticket.getTicketNumber() + " marked as no-show.");
        } else {
            System.out.println("Only waiting patients can be marked as no-show.");
        }
    }

    private void checkWaitingRoom() {
        System.out.println("Waiting room: " + monitor.getCurrentOccupancy() + "/" + monitor.getCapacity());
        if (monitor.monitorOccupancy()) monitor.alertStaff();
        else System.out.println("Occupancy is within capacity.");
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine() : "";
    }

    private QueueTicket askTicket() {
        String id = ask("Patient ID (e.g. P-001): ");
        if (!validator.validateInput(id)) {
            validator.showErrorMessage("patient ID cannot be empty.");
            return null;
        }
        QueueTicket ticket = queueManager.findByPatientID(id.trim());
        if (ticket == null) System.out.println("No patient found with ID " + id.trim() + ".");
        return ticket;
    }
}
