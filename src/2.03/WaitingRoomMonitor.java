public class WaitingRoomMonitor {
    private int currentOccupancy = 0;
    private final int capacity;

    public WaitingRoomMonitor(int capacity) { this.capacity = capacity; }

    public int getCurrentOccupancy() { return currentOccupancy; }
    public int getCapacity() { return capacity; }

    public void patientArrived() { currentOccupancy++; }
    public void patientLeft() { if (currentOccupancy > 0) currentOccupancy--; }

    public boolean monitorOccupancy() { return currentOccupancy > capacity; }

    public void alertStaff() {
        System.out.println("ALERT: Waiting room is overcrowded (" + currentOccupancy + "/" + capacity + ").");
    }
}
