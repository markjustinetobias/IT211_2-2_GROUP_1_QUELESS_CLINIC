import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    private static final int MAX_QUEUE_SIZE = 10;

    private List<QueueTicket> queueList;

    public QueueManager() {
        this.queueList = new ArrayList<>();
    }

    public List<QueueTicket> getQueueList() {
        return new ArrayList<>(queueList);      // copy, so the real list can't be changed outside
    }

    public QueueTicket manageQueue(Patient patient) {
        QueueTicket ticket = new QueueTicket(patient);
        queueList.add(ticket);
        return ticket;
    }

    public boolean checkAvailability() {
        return queueList.size() < MAX_QUEUE_SIZE;
    }

    public QueueTicket processNext() {
        if (queueList.isEmpty()) return null;
        QueueTicket next = queueList.remove(0);
        next.updateStatus("Called");
        return next;
    }
}
