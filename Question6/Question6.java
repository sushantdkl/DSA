// Traffic Signal Management System (Multithreaded)
// Functionality:
//  Queue (FIFO Scheduling): Manage vehicles at a traffic signal.
//  Priority Queue (Emergency Vehicles): Give priority to ambulances and fire trucks.
//  Multithreading:
// o Separate threads for traffic light changes, vehicle movement, and emergency
// handling.
// GUI:
//  An animated traffic intersection.
//  A queue showing waiting vehicles.
//  Buttons to:
// o Change Traffic Signal (Simulates signal changes in real-time).
// o Add Vehicles (Continuously add vehicles with a thread).
// o Enable Emergency Mode (Emergency vehicle gets priority in multithreaded execution).
// Implementation:
//  Main thread: Handles GUI and user inputs.
//  Traffic light thread: Changes signals at fixed intervals.
//  Vehicle queue thread: Processes vehicles using FIFO and priority queue logic.
// Data Structures:
//  Queue: Regular vehicle queue.
//  Priority Queue: Emergency vehicle handling.
// Multithreading Benefits:
//  Vehicles move in real-time without blocking GUI updates.
//  Traffic lights operate independently of vehicle movement.

package Question6;
import java.awt.*;
import java.util.*;
import javax.swing.*;

// Vehicle class with emergency priority
class Vehicle implements Comparable<Vehicle> {
    String type;
    boolean isEmergency;
    int id;
    long arrivalTime;

    public Vehicle(String type, boolean isEmergency) {
        this.type = type;
        this.isEmergency = isEmergency;
        this.id = (int)(Math.random() * 1000);
        this.arrivalTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return (isEmergency ? "🚨" : "🚗") + " " + type + " #" + id;
    }

    @Override
    public int compareTo(Vehicle other) {
        // Emergency vehicles have higher priority
        if (this.isEmergency && !other.isEmergency) return -1;
        if (!this.isEmergency && other.isEmergency) return 1;
        // If both are same type, FIFO order
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }
}

// Vehicle Manager with enhanced queue and priority queue
class VehicleManager {
    Queue<Vehicle> regularQueue = new LinkedList<>();
    PriorityQueue<Vehicle> emergencyQueue = new PriorityQueue<>();
    private int totalVehiclesProcessed = 0;
    private int emergencyVehiclesProcessed = 0;

    public synchronized void addVehicle(Vehicle v) {
        if (v.isEmergency) {
            emergencyQueue.add(v);
        } else {
            regularQueue.add(v);
        }
    }

    public synchronized Vehicle getNextVehicle() {
        if (!emergencyQueue.isEmpty()) {
            Vehicle v = emergencyQueue.poll();
            emergencyVehiclesProcessed++;
            totalVehiclesProcessed++;
            return v;
        }
        if (!regularQueue.isEmpty()) {
            Vehicle v = regularQueue.poll();
            totalVehiclesProcessed++;
            return v;
        }
        return null;
    }

    public synchronized java.util.List<Vehicle> getAllVehicles() {
        java.util.List<Vehicle> list = new ArrayList<>();
        list.addAll(emergencyQueue);
        list.addAll(regularQueue);
        return list;
    }

    public synchronized int getQueueSize() {
        return regularQueue.size() + emergencyQueue.size();
    }

    public synchronized int getTotalProcessed() { return totalVehiclesProcessed; }
    public synchronized int getEmergencyProcessed() { return emergencyVehiclesProcessed; }
}

// Animated Traffic Intersection Panel
class TrafficIntersectionPanel extends JPanel {
    private boolean greenLight = true;
    private final java.util.List<Vehicle> movingVehicles = new ArrayList<>();
    private int animationStep = 0;
    private final int ANIMATION_STEPS = 50;

    public TrafficIntersectionPanel() {
        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("🚦 Traffic Intersection"));
        setBackground(new Color(50, 50, 50));
    }

    public void setGreenLight(boolean green) {
        this.greenLight = green;
    }

    public void addMovingVehicle(Vehicle vehicle) {
        movingVehicles.add(vehicle);
        startAnimation();
    }

    private void startAnimation() {
        javax.swing.Timer timer = new javax.swing.Timer(50, e -> {
            animationStep++;
            if (animationStep >= ANIMATION_STEPS) {
                animationStep = 0;
                movingVehicles.clear();
                ((javax.swing.Timer)e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw roads
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 140, 400, 20); // Horizontal road
        g2d.fillRect(190, 0, 20, 300); // Vertical road

        // Draw traffic light
        g2d.setColor(Color.BLACK);
        g2d.fillRect(350, 50, 30, 80);
        g2d.setColor(greenLight ? Color.GREEN : Color.RED);
        g2d.fillOval(355, 55, 20, 20);
        g2d.setColor(greenLight ? Color.RED : Color.GREEN);
        g2d.fillOval(355, 85, 20, 20);

        // Draw moving vehicles
        for (Vehicle vehicle : movingVehicles) {
            int x = (int)(50 + (animationStep * 300.0 / ANIMATION_STEPS));
            int y = 150;
            
            if (vehicle.isEmergency) {
                g2d.setColor(Color.RED);
                g2d.fillRect(x, y, 30, 15);
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(x + 5, y - 5, 20, 5);
            } else {
                g2d.setColor(Color.BLUE);
                g2d.fillRect(x, y, 25, 12);
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 8));
            g2d.drawString(vehicle.type, x + 2, y + 8);
        }

        // Draw intersection lines
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, 150, 190, 150);
        g2d.drawLine(210, 150, 400, 150);
        g2d.drawLine(200, 0, 200, 140);
        g2d.drawLine(200, 160, 200, 300);
    }
}

// Enhanced Traffic signal controller thread
class SignalController extends Thread {
    private volatile boolean greenLight = true;
    private volatile boolean running = true;
    private final TrafficIntersectionPanel intersectionPanel;
    private final JLabel statusLabel;

    public SignalController(TrafficIntersectionPanel panel, JLabel statusLabel) {
        this.intersectionPanel = panel;
        this.statusLabel = statusLabel;
        setDaemon(true);
    }

    public void stopController() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            greenLight = true;
            SwingUtilities.invokeLater(() -> {
                intersectionPanel.setGreenLight(true);
                statusLabel.setText("🟢 GREEN Light - Vehicles can proceed");
                statusLabel.setForeground(Color.GREEN);
            });
            
            try { 
                Thread.sleep(5000); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt();
                break; 
            }
            
            greenLight = false;
            SwingUtilities.invokeLater(() -> {
                intersectionPanel.setGreenLight(false);
                statusLabel.setText("🔴 RED Light - Vehicles must wait");
                statusLabel.setForeground(Color.RED);
            });
            
            try { 
                Thread.sleep(3000); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt();
                break; 
            }
        }
    }

    public boolean isGreen() {
        return greenLight;
    }
}

// Enhanced Vehicle processor thread
class VehicleProcessor extends Thread {
    private final VehicleManager manager;
    private final SignalController signal;
    private final TrafficIntersectionPanel intersectionPanel;
    private final JTextArea logArea;
    private final Runnable updateQueue;
    private volatile boolean running = true;

    public VehicleProcessor(VehicleManager manager, SignalController signal,
                            TrafficIntersectionPanel intersectionPanel,
                            JTextArea logArea, Runnable updateQueue) {
        this.manager = manager;
        this.signal = signal;
        this.intersectionPanel = intersectionPanel;
        this.logArea = logArea;
        this.updateQueue = updateQueue;
        setDaemon(true);
    }

    public void stopProcessor() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (signal.isGreen()) {
                Vehicle v = manager.getNextVehicle();
                if (v != null) {
                    final Vehicle vehicle = v;
                    SwingUtilities.invokeLater(() -> {
                        String msg = "⏩ " + vehicle + " passed the intersection.\n";
                        logArea.append(msg);
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                        intersectionPanel.addMovingVehicle(vehicle);
                        updateQueue.run();
                    });
                }
            }
            try { 
                Thread.sleep(1000); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt();
                break; 
            }
        }
    }
}

// Vehicle Generator Thread
class VehicleGenerator extends Thread {
    private final VehicleManager manager;
    private volatile boolean running = true;
    private volatile boolean emergencyMode = false;

    public VehicleGenerator(VehicleManager manager) {
        this.manager = manager;
        setDaemon(true);
    }

    public void stopGenerator() {
        running = false;
    }

    public void setEmergencyMode(boolean emergency) {
        this.emergencyMode = emergency;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(2000 + (int)(Math.random() * 3000)); // Random interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
                
            if (emergencyMode && Math.random() < 0.3) {
                // Add emergency vehicle
                String[] emergencyTypes = {"🚑 Ambulance", "🚒 Fire Truck", "🚓 Police Car"};
                String type = emergencyTypes[(int)(Math.random() * emergencyTypes.length)];
                manager.addVehicle(new Vehicle(type, true));
            } else {
                // Add regular vehicle
                String[] regularTypes = {"🚗 Car", "🚐 Van", "🚛 Truck", "🏍️ Motorcycle"};
                String type = regularTypes[(int)(Math.random() * regularTypes.length)];
                manager.addVehicle(new Vehicle(type, false));
            }
        }
    }
}

// Main GUI with Enhanced Features
public class Question6 extends JFrame {
    private VehicleManager manager = new VehicleManager();
    private final TrafficIntersectionPanel intersectionPanel = new TrafficIntersectionPanel();
    private SignalController signalThread;
    private VehicleProcessor processorThread;
    private VehicleGenerator generatorThread;
    
    private final JTextArea queueArea = new JTextArea(12, 30);
    private JTextArea logArea = new JTextArea(8, 30);
    private final JLabel statusLabel = new JLabel("🟢 GREEN Light - Vehicles can proceed");
    private final JLabel statsLabel = new JLabel("📊 Statistics: Total: 0 | Emergency: 0 | Queue: 0");

    public Question6() {
        super("🚦 Traffic Signal Management System (Multithreaded)");

        // Initialize components
        queueArea.setEditable(false);
        logArea.setEditable(false);
        queueArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Create control buttons
        JButton addCarBtn = new JButton("🚗 Add Regular Vehicle");
        JButton addEmergencyBtn = new JButton("🚨 Add Emergency Vehicle");
        JButton emergencyModeBtn = new JButton("🚨 Enable Emergency Mode");
        JButton stopBtn = new JButton("⏹️ Stop All");
        JButton clearBtn = new JButton("🧹 Clear Log");

        // Style buttons
        addCarBtn.setBackground(new Color(100, 150, 255));
        addEmergencyBtn.setBackground(new Color(255, 100, 100));
        emergencyModeBtn.setBackground(new Color(255, 200, 100));
        stopBtn.setBackground(new Color(200, 200, 200));
        clearBtn.setBackground(new Color(150, 255, 150));

        // Add action listeners
        addCarBtn.addActionListener(_ -> {
            String[] types = {"🚗 Car", "🚐 Van", "🚛 Truck", "🏍️ Motorcycle"};
            String type = types[(int)(Math.random() * types.length)];
            manager.addVehicle(new Vehicle(type, false));
            updateQueueDisplay();
        });

        addEmergencyBtn.addActionListener(_ -> {
            String[] types = {"🚑 Ambulance", "🚒 Fire Truck", "🚓 Police Car"};
            String type = types[(int)(Math.random() * types.length)];
            manager.addVehicle(new Vehicle(type, true));
            updateQueueDisplay();
        });

        emergencyModeBtn.addActionListener(_ -> {
            if (generatorThread != null) {
                boolean currentMode = !emergencyModeBtn.getText().contains("Disable");
                generatorThread.setEmergencyMode(currentMode);
                emergencyModeBtn.setText(currentMode ? "🚨 Disable Emergency Mode" : "🚨 Enable Emergency Mode");
            }
        });

        stopBtn.addActionListener(_ -> stopAllThreads());
        clearBtn.addActionListener(_ -> logArea.setText(""));

        // Layout
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(addCarBtn);
        controlPanel.add(addEmergencyBtn);
        controlPanel.add(emergencyModeBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(clearBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(intersectionPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(queueArea), BorderLayout.CENTER);
        rightPanel.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(statsLabel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // Start threads
        startThreads();

        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startThreads() {
        signalThread = new SignalController(intersectionPanel, statusLabel);
        processorThread = new VehicleProcessor(manager, signalThread, intersectionPanel, logArea, this::updateQueueDisplay);
        generatorThread = new VehicleGenerator(manager);
        
        signalThread.start();
        processorThread.start();
        generatorThread.start();
    }

    private void stopAllThreads() {
        if (signalThread != null) signalThread.stopController();
        if (processorThread != null) processorThread.stopProcessor();
        if (generatorThread != null) generatorThread.stopGenerator();
        
        logArea.append("⏹️ All threads stopped.\n");
    }

    private void updateQueueDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("🚦 VEHICLE QUEUE:\n");
        sb.append("================\n\n");
        
        java.util.List<Vehicle> vehicles = manager.getAllVehicles();
        if (vehicles.isEmpty()) {
            sb.append("No vehicles waiting\n");
        } else {
            for (Vehicle v : vehicles) {
                sb.append(v.toString()).append("\n");
            }
        }
        
        queueArea.setText(sb.toString());
        
        // Update statistics
        int total = manager.getTotalProcessed();
        int emergency = manager.getEmergencyProcessed();
        int queueSize = manager.getQueueSize();
        statsLabel.setText(String.format("📊 Statistics: Total: %d | Emergency: %d | Queue: %d", 
            total, emergency, queueSize));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question6::new);
    }
}
