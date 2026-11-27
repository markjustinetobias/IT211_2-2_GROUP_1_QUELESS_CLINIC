
public class CoreClassTest {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    public static void main(String[] args) {

        System.out.println("===== CORE CLASS TEST =====\n");

        // 1. Patient
        Patient patient1 = new Patient("Juan Dela Cruz", "P-001");

        check("Patient name getter",
                patient1.getName().equals("Juan Dela Cruz"));

        check("Patient ID getter",
                patient1.getPatientID().equals("P-001"));

        check("Patient initially not in queue",
                patient1.checkStatus().equals("Not in queue"));

        // 2. QueueTicket
        QueueManager manager = new QueueManager();
        QueueTicket ticket1 = patient1.requestQueue(manager);

        check("Ticket starts with Waiting status",
                ticket1.getStatus().equals("Waiting"));

        check("Ticket patient association",
                ticket1.getPatient() == patient1);

        check("Ticket is not priority",
                !ticket1.isPriority());

        check("Ticket position is 1",
                ticket1.getPosition(manager) == 1);

        // 3. QueueManager
        check("Queue contains one patient",
                manager.waitingCount() == 1);

        check("Total registered is 1",
                manager.totalRegistered() == 1);

        Patient patient2 = new Patient("Maria Santos", "P-002");
        QueueTicket ticket2 = patient2.requestQueue(manager);

        check("FIFO position of second patient",
                ticket2.getPosition(manager) == 2);

        check("Queue count is 2",
                manager.waitingCount() == 2);

        check("Queue is available",
                manager.checkAvailability());

        QueueTicket called = manager.processNext();

        check("First patient is processed",
                called == ticket1);

        check("Patient status becomes Called",
                ticket1.getStatus().equals("Called"));

        check("Active ticket is identified",
                manager.findActive() == ticket1);

        check("Cannot call another patient while active",
                manager.processNext() == null);

        // Cancellation and No-show
        Patient patient3 = new Patient("Pedro Reyes", "P-003");
        QueueTicket ticket3 = patient3.requestQueue(manager);

        check("Waiting patient can be cancelled",
                manager.markCancelled(ticket3));

        check("Cancelled status is recorded",
                ticket3.getStatus().equals("Cancelled"));

        Patient patient4 = new Patient("Ana Lopez", "P-004");
        QueueTicket ticket4 = patient4.requestQueue(manager);

        check("Waiting patient can be marked no-show",
                manager.markNoShow(ticket4));

        check("No-show status is recorded",
                ticket4.getStatus().equals("No-show"));

        // Queue capacity
        QueueManager fullManager = new QueueManager();

        for (int i = 1; i <= 10; i++) {
            Patient p = new Patient("Patient " + i, "F-" + i);
            p.requestQueue(fullManager);
        }

        check("Queue reaches maximum capacity",
                fullManager.waitingCount() == 10);

        check("Full queue is unavailable",
                !fullManager.checkAvailability());

        // 4. ClinicStaff

        // Finish the first patient's consultation before testing staff call-next
        ticket1.updateStatus("Completed");
        
        ClinicStaff staff = new ClinicStaff("S-001", "Nurse Ana");

        QueueTicket staffCalled = staff.callPatient(manager);

        check("Staff can call next patient",
                staffCalled == ticket2);

        check("Called advances to In Consultation",
                staff.advanceConsultation(manager, ticket2)
                        .equals("In Consultation"));

        check("In Consultation advances to Completed",
                staff.advanceConsultation(manager, ticket2)
                        .equals("Completed"));

        check("Completed status cannot advance further",
                staff.advanceConsultation(manager, ticket2) == null);

        // 5. PriorityRequest
        Patient priorityPatient = new Patient("Emergency Patient", "P-005");

        PriorityRequest request = new PriorityRequest(
                "PR-001", priorityPatient, "Emergency");

        check("Priority request starts Pending",
                request.getStatus().equals("Pending"));

        check("Priority request is approved",
                staff.approvePriority(request));

        check("Priority request status is Approved",
                request.getStatus().equals("Approved"));

        check("Approving staff is recorded",
                request.getApprovedBy() == staff);

        // Priority queue ordering
        QueueManager priorityManager = new QueueManager();

        Patient ordinary = new Patient("Ordinary", "O-001");
        ordinary.requestQueue(priorityManager);

        Patient urgent = new Patient("Urgent", "U-001");
        PriorityRequest urgentRequest = new PriorityRequest(
                "PR-002", urgent, "Urgent case");

        staff.approvePriority(urgentRequest);
        QueueTicket priorityTicket =
                priorityManager.manageQueue(urgent, urgentRequest);

        check("Priority patient is placed ahead",
                priorityManager.getQueueList().get(0) == priorityTicket);

        // 6. WaitingRoomMonitor
        WaitingRoomMonitor monitor = new WaitingRoomMonitor(2);

        monitor.patientArrived();
        monitor.patientArrived();

        check("Occupancy counts arrivals",
                monitor.getCurrentOccupancy() == 2);

        monitor.patientArrived();

        check("Overcapacity is detected",
                monitor.monitorOccupancy());

        monitor.patientLeft();

        check("Occupancy decreases after departure",
                monitor.getCurrentOccupancy() == 2);

        // 7. SessionReport
        SessionReport report = new SessionReport(
                "R-001", manager, monitor);

        check("Report includes registered patients",
                report.generateReport().contains(
                        "Total registered patients"));

        // 8. InputValidator
        InputValidator validator = new InputValidator();

        check("Validator accepts normal text",
                validator.validateInput("hello"));

        check("Validator rejects null, empty and whitespace",
                !validator.validateInput(null)
                && !validator.validateInput("")
                && !validator.validateInput("   "));

        check("Menu choice validation works",
                validator.validateMenuChoice("3", 0, 8)
                && !validator.validateMenuChoice("9", 0, 8));

        // Final result
        System.out.println("\n===== TEST SUMMARY =====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed == 0) {
            System.out.println("RESULT: ALL TESTS PASSED");
        } else {
            System.out.println("RESULT: TESTS FAILED");
        }
    }
}
