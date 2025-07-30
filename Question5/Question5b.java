// Online Ticket Booking System – Concurrency Control
// Functionality:
//  Locks (Mutex / Semaphore): Ensure multiple users don't book the same seat simultaneously.
//  Queue (Booking Requests): Manage pending seat reservation requests.
//  Database (Shared Resource): Store and update seat availability status concurrently.
// GUI:
//  A seating chart displaying available and booked seats.
//  A queue showing pending booking requests.
//  Buttons to:
// o Book a Seat (Simulate multiple users trying to book seats).
// o Enable Concurrency Control (Optimistic or Pessimistic Locking).
// o Process Bookings (Execute transactions concurrently).
// Implementation:
// Initialization:
// 1. Generate a seating layout for a theater/train/flight with available seats.
// 2. Create a queue of booking requests from multiple users.
// 3. Allow the user to choose a concurrency control mechanism (optimistic or pessimistic locking).
// 4. Display the seat availability in the GUI.
// Booking Process:
// 1. Choose a Concurrency Control Mechanism:
// o Optimistic Locking:
//   Read seat availability → Attempt to book → Check if status changed →
// Commit or retry.
// o Pessimistic Locking:
//   Lock the seat → Process booking → Unlock after completion.
// 2. Process Booking Requests:
// o Fetch a request from the queue.
// o Apply the chosen concurrency mechanism.
// o Update the seat status safely.
// 3. Real-time GUI Updates:
// o Show updated seat availability.
// o Handle failures if a seat is already booked.
// Booking Completion:
//  If a seat is successfully booked, confirm the booking.
//  If a conflict arises, retry or notify the user.
// Data Structures:
//  Queue: Store pending booking requests before processing.
//  HashMap / Dictionary: Maintain seat availability status.
//  Mutex / Semaphore: Prevent race conditions during seat selection.
//  Thread Pool: Simulate multiple users booking seats concurrently.
// Additional Considerations:
//  Deadlock Prevention: Handle timeout or avoid circular waits.
//  Transaction Logging: Keep a record of successful and failed bookings.
//  Performance Monitoring: Display success rate, conflicts, and retries.
//  Refund and Cancellation Handling: Allow users to cancel bookings and free up seats.

package Question5;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;

// Booking Request Object
class BookingRequest {
    String userId;
    int seatNumber;

    public BookingRequest(String userId, int seatNumber) {
        this.userId = userId;
        this.seatNumber = seatNumber;
    }
}

// Seat Manager with Locking Strategies
class SeatManager {
    Map<Integer, Boolean> seats = new ConcurrentHashMap<>();
    final Object lock = new Object();

    public SeatManager(int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) seats.put(i, false);
    }

    public boolean bookSeatPessimistic(int seatNumber) {
        synchronized (lock) {
            if (!seats.get(seatNumber)) {
                seats.put(seatNumber, true);
                return true;
            }
            return false;
        }
    }

    public boolean bookSeatOptimistic(int seatNumber) {
        if (!seats.get(seatNumber)) {
            if (Math.random() > 0.2) { // simulate conflict
                synchronized (lock) {
                    if (!seats.get(seatNumber)) {
                        seats.put(seatNumber, true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Map<Integer, Boolean> getSeats() {
        return seats;
    }
}

// Booking Processor Thread
class BookingProcessor implements Runnable {
    BlockingQueue<BookingRequest> queue;
    SeatManager manager;
    boolean useOptimistic;
    JTextArea logArea;
    Runnable updateDisplay;

    public BookingProcessor(
        BlockingQueue<BookingRequest> queue,
        SeatManager manager,
        boolean useOptimistic,
        JTextArea logArea,
        Runnable updateDisplay
    ) {
        this.queue = queue;
        this.manager = manager;
        this.useOptimistic = useOptimistic;
        this.logArea = logArea;
        this.updateDisplay = updateDisplay;
    }

    public void run() {
        while (!queue.isEmpty()) {
            try {
                BookingRequest request = queue.take();
                boolean success = useOptimistic
                    ? manager.bookSeatOptimistic(request.seatNumber)
                    : manager.bookSeatPessimistic(request.seatNumber);

                String msg = "User " + request.userId + " tried Seat " + request.seatNumber +
                    " → " + (success ? "✅ Booked" : "❌ Failed") + "\n";

                SwingUtilities.invokeLater(() -> {
                    logArea.append(msg);
                    updateDisplay.run();
                });

                Thread.sleep(200); // simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Main GUI
public class Question5b extends JFrame {
    SeatManager seatManager = new SeatManager(40);
    BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();
    boolean useOptimisticLocking = true;

    JTextArea statusArea = new JTextArea(20, 30);
    JTextArea logArea = new JTextArea(10, 30);

    public Question5b() {
        super("🎟️ Online Ticket Booking System");

        statusArea.setEditable(false);
        logArea.setEditable(false);

        JButton simulateBtn = new JButton("Simulate Bookings");
        JButton processBtn = new JButton("Process Bookings");
        JButton toggleBtn = new JButton("Toggle Locking");
        JLabel lockLabel = new JLabel("🔒 Mode: Optimistic");

        simulateBtn.addActionListener(e -> {
            for (int i = 1; i <= 10; i++) {
                int seat = (int)(Math.random() * 40) + 1;
                bookingQueue.add(new BookingRequest("User" + i, seat));
            }
        });

        processBtn.addActionListener(e -> {
            new Thread(new BookingProcessor(
                bookingQueue,
                seatManager,
                useOptimisticLocking,
                logArea,
                this::refreshSeatDisplay
            )).start();
        });

        toggleBtn.addActionListener(e -> {
            useOptimisticLocking = !useOptimisticLocking;
            lockLabel.setText("🔒 Mode: " +
                (useOptimisticLocking ? "Optimistic" : "Pessimistic"));
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(simulateBtn);
        controlPanel.add(processBtn);
        controlPanel.add(toggleBtn);
        controlPanel.add(lockLabel);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(new JScrollPane(statusArea));
        textPanel.add(new JScrollPane(logArea));

        add(controlPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        refreshSeatDisplay();

        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void refreshSeatDisplay() {
        StringBuilder sb = new StringBuilder();
        seatManager.getSeats().forEach((k, v) ->
            sb.append("Seat ").append(k).append(": ")
              .append(v ? "Booked ✅" : "Available 🟢").append("\n"));
        statusArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question5b::new);
    }
}
