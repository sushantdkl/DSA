// Scenario: Weather Anomaly Detection 🌦️📊
// A climate scientist is analyzing temperature variations over a given period to detect unusual patterns
// in weather changes.
// The scientist has a dataset containing the daily temperature changes (increase or decrease in °C)
// relative to the previous day.
// They want to count the number of continuous time periods where the total temperature change falls
// within a specified anomaly range
// [𝑙𝑜𝑤_𝑡ℎ𝑟𝑒𝑠ℎ𝑜𝑙𝑑,ℎ𝑖𝑔ℎ_𝑡ℎ𝑟𝑒𝑠ℎ𝑜𝑙𝑑]
// Each period is defined as a continuous range of days, and the total anomaly for that period is the sum
// of temperature changes within that range.
// Example 1
// Input:
// temperature_changes = [3, -1, -4, 6, 2]
// low_threshold = 2
// high_threshold = 5
// Output: 3
// Explanation:
// We consider all possible subarrays and their total temperature change:
// Day 0 to Day 0 → Total change = 3 ✅ (within range [2, 5])
// Day 3 to Day 3 → Total change = 6 ❌ (out of range)
// Day 3 to Day 4 → Total change = 6 + 2 = 8 ❌ (out of range)
// Day 1 to Day 3 → Total change = (-1) + (-4) + 6 = 1 ❌ (out of range)
// Day 2 to Day 4 → Total change = (-4) + 6 + 2 = 4 ✅ (within range [2, 5])
// Day 1 to Day 4 → Total change = (-1) + (-4) + 6 + 2 = 3 ✅ (within range [2,5 ])
// Day 0 to Day 2 → Total change = 3 + (-1) + (-4) = -2 ❌ (out of range)
// Day 0 to Day 4 → Total change = 3 + (-1) + (-4) + 6 + 2 = 6 ❌ (out of range)
// Thus, total valid periods = 4.
// Example 2
// Input:
// temperature_changes = [-2, 3, 1, -5, 4]
// low_threshold = -1
// high_threshold = 2
// Output: 4
// Explanation:
// Valid subarrays where the total temperature change falls within [-1, 2]:
// Day 1 to Day 2 → Total change = 3 + 1 = 4 ❌ (out of range)
// Day 2 to Day 3 → Total change = 1 + (-5) = -4 ❌ (out of range)
// Day 1 to Day 3 → Total change = 3 + 1 + (-5) = -1 ✅
// Day 2 to Day 4 → Total change = 1 + (-5) + 4 = 0 ✅
// Day 0 to Day 2 → Total change = (-2) + 3 + 1 = 2 ✅
// Day 1 to Day 4 → Total change = 3 + 1 + (-5) + 4 = 3 ❌ (out of range)
// Day 0 to Day 4 → Total change = (-2) + 3 + 1 + (-5) + 4 = 1 ✅
// Thus, total valid periods = 5.

package Question2;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Question2a extends JFrame {
    private JTextField tempChangesField, lowThresholdField, highThresholdField;
    private JTextArea resultArea;
    private JButton analyzeBtn, clearBtn, exampleBtn;
    private JPanel mainPanel, inputPanel, buttonPanel, resultPanel;
    private JPanel chartPanel;
    
    public Question2a() {
        setTitle("🌦️ Weather Anomaly Detection System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 750);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Input fields with modern styling
        tempChangesField = createStyledTextField("3,-1,-4,6,2");
        lowThresholdField = createStyledTextField("2");
        highThresholdField = createStyledTextField("5");
        
        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(248, 249, 250));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Chart panel for visualization
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTemperatureChart(g);
            }
        };
        chartPanel.setPreferredSize(new Dimension(700, 200));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(108, 117, 125), 1));
        
        // Buttons with modern styling
        analyzeBtn = createStyledButton("🔍 Analyze Anomalies", new Color(40, 167, 69));
        clearBtn = createStyledButton("🗑️ Clear", new Color(220, 53, 69));
        exampleBtn = createStyledButton("📝 Load Examples", new Color(0, 123, 255));
    }
    
    private JTextField createStyledTextField(String defaultText) {
        JTextField field = new JTextField(defaultText, 20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        
        // Main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 248, 255), 
                                                         getWidth(), getHeight(), new Color(255, 248, 240));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Input panel
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("🌦️ Weather Anomaly Detection System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Climate Pattern Analysis Tool");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);
        
        // Input fields
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        inputPanel.add(createInputRow("Temperature Changes (°C):", tempChangesField), gbc);
        gbc.gridy = 3;
        inputPanel.add(createInputRow("Low Threshold:", lowThresholdField), gbc);
        gbc.gridy = 4;
        inputPanel.add(createInputRow("High Threshold:", highThresholdField), gbc);
        
        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(analyzeBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exampleBtn);
        
        // Result panel
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setOpaque(false);
        JLabel resultLabel = new JLabel("📊 Analysis Results:");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(33, 37, 41));
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        
        // Create a panel for chart and results
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(chartPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        resultPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(73, 80, 87));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        analyzeBtn.addActionListener(_ -> analyzeAnomalies());
        clearBtn.addActionListener(_ -> clearAll());
        exampleBtn.addActionListener(_ -> loadExamples());
        
        // Enter key support
        KeyAdapter enterKey = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    analyzeAnomalies();
                }
            }
        };
        
        tempChangesField.addKeyListener(enterKey);
        lowThresholdField.addKeyListener(enterKey);
        highThresholdField.addKeyListener(enterKey);
    }
    
    private void drawTemperatureChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        try {
            int[] temps = parseTemperatureArray(tempChangesField.getText());
            int low = Integer.parseInt(lowThresholdField.getText().trim());
            int high = Integer.parseInt(highThresholdField.getText().trim());
            
            int width = chartPanel.getWidth() - 40;
            int height = chartPanel.getHeight() - 60;
            int x0 = 20, y0 = 30;
            
            if (temps.length == 0) return;
            
            // Find min and max for scaling
            int minTemp = Arrays.stream(temps).min().getAsInt();
            int maxTemp = Arrays.stream(temps).max().getAsInt();
            int range = Math.max(maxTemp - minTemp, 1);
            
            // Draw axes
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x0, y0 + height, x0 + width, y0 + height); // X-axis
            g2d.drawLine(x0, y0, x0, y0 + height); // Y-axis
            
            // Draw threshold lines
            g2d.setColor(new Color(255, 193, 7));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            int yLow = y0 + height - (int)((low - minTemp) * height / range);
            int yHigh = y0 + height - (int)((high - minTemp) * height / range);
            g2d.drawLine(x0, yLow, x0 + width, yLow);
            g2d.drawLine(x0, yHigh, x0 + width, yHigh);
            
            // Draw temperature line
            g2d.setColor(new Color(0, 123, 255));
            g2d.setStroke(new BasicStroke(3));
            
            int xStep = width / (temps.length - 1);
            for (int i = 0; i < temps.length - 1; i++) {
                int x1 = x0 + i * xStep;
                int x2 = x0 + (i + 1) * xStep;
                int y1 = y0 + height - (int)((temps[i] - minTemp) * height / range);
                int y2 = y0 + height - (int)((temps[i + 1] - minTemp) * height / range);
                g2d.drawLine(x1, y1, x2, y2);
            }
            
            // Draw points
            g2d.setColor(new Color(220, 53, 69));
            for (int i = 0; i < temps.length; i++) {
                int x = x0 + i * xStep;
                int y = y0 + height - (int)((temps[i] - minTemp) * height / range);
                g2d.fillOval(x - 4, y - 4, 8, 8);
                
                // Draw temperature values
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.drawString(String.valueOf(temps[i]), x - 10, y - 8);
                g2d.setColor(new Color(220, 53, 69));
            }
            
            // Draw labels
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Temperature Changes (°C)", width/2 + x0 - 50, y0 + height + 20);
            g2d.drawString("Days", 10, y0 + height/2);
            
        } catch (Exception e) {
            // If parsing fails, just draw empty chart
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("Enter valid data to see chart", chartPanel.getWidth()/2 - 80, chartPanel.getHeight()/2);
        }
        
        g2d.dispose();
    }
    
    private int[] parseTemperatureArray(String input) {
        String[] parts = input.trim().split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }
    
    private void analyzeAnomalies() {
        try {
            int[] temperature_changes = parseTemperatureArray(tempChangesField.getText().trim());
            int low = Integer.parseInt(lowThresholdField.getText().trim());
            int high = Integer.parseInt(highThresholdField.getText().trim());
            
            if (low > high) {
                showError("Low threshold cannot be greater than high threshold!");
                return;
            }
            
            int result = countAnomalousPeriods(temperature_changes, low, high);
            
            StringBuilder sb = new StringBuilder();
            sb.append("🌦️ WEATHER ANOMALY ANALYSIS\n");
            sb.append("==========================\n\n");
            
            sb.append("📊 INPUT DATA:\n");
            sb.append("• Temperature changes: [");
            for (int i = 0; i < temperature_changes.length; i++) {
                sb.append(temperature_changes[i]);
                if (i < temperature_changes.length - 1) sb.append(", ");
            }
            sb.append("]\n");
            sb.append("• Anomaly range: [").append(low).append(", ").append(high).append("]\n");
            sb.append("• Number of days: ").append(temperature_changes.length).append("\n\n");
            
            sb.append("🔍 DETAILED ANALYSIS:\n");
            List<String> validPeriods = new ArrayList<>();
            int totalPeriods = 0;
            
            for (int start = 0; start < temperature_changes.length; start++) {
                int sum = 0;
                for (int end = start; end < temperature_changes.length; end++) {
                    sum += temperature_changes[end];
                    totalPeriods++;
                    
                    if (sum >= low && sum <= high) {
                        validPeriods.add(String.format("Days %d-%d: sum=%d ✅", start, end, sum));
                    }
                }
            }
            
            sb.append("• Total possible periods: ").append(totalPeriods).append("\n");
            sb.append("• Valid anomalous periods: ").append(validPeriods.size()).append("\n\n");
            
            sb.append("✅ VALID PERIODS:\n");
            for (String period : validPeriods) {
                sb.append("• ").append(period).append("\n");
            }
            sb.append("\n");
            
            sb.append("🎯 FINAL RESULT:\n");
            sb.append("Number of anomalous periods: ").append(result).append("\n\n");
            
            sb.append("💡 ALGORITHM EXPLANATION:\n");
            sb.append("1. Use prefix sum technique for efficient subarray sum calculation\n");
            sb.append("2. For each position, find how many previous prefix sums fall in valid range\n");
            sb.append("3. Use TreeMap to maintain sorted prefix sums for range queries\n");
            
            resultArea.setText(sb.toString());
            chartPanel.repaint();
            
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for all fields!");
        } catch (Exception ex) {
            showError("An error occurred: " + ex.getMessage());
        }
    }
    
    private void clearAll() {
        tempChangesField.setText("3,-1,-4,6,2");
        lowThresholdField.setText("2");
        highThresholdField.setText("5");
        resultArea.setText("");
        chartPanel.repaint();
    }
    
    private void loadExamples() {
        String[] examples = {
            "Example 1: 3,-1,-4,6,2 (Low:2, High:5)",
            "Example 2: -2,3,1,-5,4 (Low:-1, High:2)",
            "Example 3: 1,2,3,4,5 (Low:5, High:15)"
        };
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Choose an example:",
            "Load Example",
            JOptionPane.QUESTION_MESSAGE,
            null,
            examples,
            examples[0]
        );
        
        if (selected != null) {
            if (selected.contains("Example 1")) {
                tempChangesField.setText("3,-1,-4,6,2");
                lowThresholdField.setText("2");
                highThresholdField.setText("5");
            } else if (selected.contains("Example 2")) {
                tempChangesField.setText("-2,3,1,-5,4");
                lowThresholdField.setText("-1");
                highThresholdField.setText("2");
            } else if (selected.contains("Example 3")) {
                tempChangesField.setText("1,2,3,4,5");
                lowThresholdField.setText("5");
                highThresholdField.setText("15");
            }
            analyzeAnomalies();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int countAnomalousPeriods(int[] temperature_changes, int low, int high) {
        TreeMap<Long, Integer> prefixMap = new TreeMap<>();
        prefixMap.put(0L, 1);  // Base case: empty subarray

        long prefixSum = 0;
        int count = 0;

        for (int change : temperature_changes) {
            prefixSum += change;

            long from = prefixSum - high;
            long to = prefixSum - low;

            // Count how many previous prefix sums are in the valid range
            for (Map.Entry<Long, Integer> entry : prefixMap.subMap(from, true, to, true).entrySet()) {
                count += entry.getValue();
            }

            // Add current prefix sum to the map
            prefixMap.put(prefixSum, prefixMap.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question2a());
    }
}
