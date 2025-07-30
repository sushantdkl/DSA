/*
Maximizing Tech Startup Revenue before Acquisition
A tech startup, AlgoStart, is planning to get acquired by a larger company. To negotiate a higher
acquisition price, AlgoStart wants to increase its revenue by launching a few high-return projects.
However, due to limited resources, the startup can only work on at most k distinct projects before the
acquisition.
You are given n potential projects, where the i-th project has a projected revenue gain of revenues[i]
and requires a minimum investment capital of investments[i] to launch.
Initially, AlgoStart has c capital. When a project is completed, its revenue gain is added to the
startup's total capital, which can then be reinvested into other projects.
Your task is to determine the maximum possible capital AlgoStart can accumulate after completing at
most k projects.
Example 1:
k = 2, c = 0, revenues = [2, 5, 8], investments = [0, 2, 3]
Output: 7
Explanation:
 With initial capital 0, the startup can only launch Project 0 (since it requires 0 investment).
 After completing Project 0, the capital becomes 0 + 2 = 2.
 Now, with 2 capital, the startup can choose either Project 1 (investment 2, revenue 5) or
Project 2 (investment 3, revenue 8).
 To maximize revenue, it should select Project 2. However, Project 2 requires 3 capital, which is
not available. So it selects Project 1.
 After completing Project 1, the capital becomes 2 + 5 = 7.
 The final maximized capital is 7.
Example 2:
Input:
k = 3, c = 1, revenues = [3, 6, 10], investments = [1, 3, 5]
Output: 19
Explanation:
 Initially, with 1 capital, Project 0 can be launched (investment 1, revenue 3).
 Capital after Project 0 = 1 + 3 = 4.
 With 4 capital, the startup can now launch Project 1 (investment 3, revenue 6).
 Capital after Project 1 = 4 + 6 = 10.
 Now, with 10 capital, Project 2 (investment 5, revenue 10) can be launched.
 Final capital = 10 + 10 = 19.*/

package Question1;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Question1 extends JFrame {
    private JTextField kField, capitalField, revenuesField, investmentsField;
    private JTextArea resultArea;
    private JButton calculateBtn, clearBtn, exampleBtn;
    private JPanel mainPanel, inputPanel, buttonPanel, resultPanel;
    
    public Question1() {
        setTitle("🚀 Tech Startup Revenue Maximizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Input fields with modern styling
        kField = createStyledTextField("2");
        capitalField = createStyledTextField("0");
        revenuesField = createStyledTextField("2,5,8");
        investmentsField = createStyledTextField("0,2,3");
        
        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(new Color(248, 249, 250));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Buttons with modern styling
        calculateBtn = createStyledButton("Calculate Maximum Capital", new Color(40, 167, 69));
        clearBtn = createStyledButton("Clear All", new Color(220, 53, 69));
        exampleBtn = createStyledButton("Load Example", new Color(0, 123, 255));
    }
    
    private JTextField createStyledTextField(String defaultText) {
        JTextField field = new JTextField(defaultText, 20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
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
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 248, 240), 
                                                         getWidth(), getHeight(), new Color(240, 248, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input panel
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Tech Startup Revenue Maximizer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(titleLabel, gbc);
        
        // Input fields
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        inputPanel.add(createInputRow("Maximum Projects (k):", kField), gbc);
        gbc.gridy = 2;
        inputPanel.add(createInputRow("Initial Capital (c):", capitalField), gbc);
        gbc.gridy = 3;
        inputPanel.add(createInputRow("Revenue Gains (comma-separated):", revenuesField), gbc);
        gbc.gridy = 4;
        inputPanel.add(createInputRow("Investment Requirements (comma-separated):", investmentsField), gbc);
        
        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exampleBtn);
        
        // Result panel
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setOpaque(false);
        JLabel resultLabel = new JLabel("Results:");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(33, 37, 41));
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        // Add panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        calculateBtn.addActionListener(_ -> calculateResult());
        clearBtn.addActionListener(_ -> clearAll());
        exampleBtn.addActionListener(_ -> loadExample());
        
        // Enter key support
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculateResult();
                }
            }
        };
        
        kField.addKeyListener(enterKey);
        capitalField.addKeyListener(enterKey);
        revenuesField.addKeyListener(enterKey);
        investmentsField.addKeyListener(enterKey);
    }
    
    private void calculateResult() {
        try {
            int k = Integer.parseInt(kField.getText().trim());
            int c = Integer.parseInt(capitalField.getText().trim());
            
            String[] revenuesStr = revenuesField.getText().trim().split(",");
            String[] investmentsStr = investmentsField.getText().trim().split(",");
            
            if (revenuesStr.length != investmentsStr.length) {
                showError("Revenue and investment arrays must have the same length!");
                return;
            }
            
            int[] revenues = new int[revenuesStr.length];
            int[] investments = new int[investmentsStr.length];
            
            for (int i = 0; i < revenuesStr.length; i++) {
                revenues[i] = Integer.parseInt(revenuesStr[i].trim());
                investments[i] = Integer.parseInt(investmentsStr[i].trim());
            }
            
            int result = maximizeCapital(k, c, revenues, investments);
            
            StringBuilder sb = new StringBuilder();
            sb.append("📊 ANALYSIS RESULTS\n");
            sb.append("==================\n\n");
            sb.append("Input Parameters:\n");
            sb.append("• Maximum projects (k): ").append(k).append("\n");
            sb.append("• Initial capital (c): ").append(c).append("\n");
            sb.append("• Number of projects: ").append(revenues.length).append("\n\n");
            
            sb.append("Project Details:\n");
            for (int i = 0; i < revenues.length; i++) {
                sb.append(String.format("• Project %d: Investment=%d, Revenue=%d\n", i, investments[i], revenues[i]));
            }
            sb.append("\n");
            
            sb.append("🎯 RESULT:\n");
            sb.append("Maximum possible capital: ").append(result).append("\n\n");
            
            sb.append("Algorithm Explanation:\n");
            sb.append("1. Sort projects by investment requirement (ascending)\n");
            sb.append("2. Use max-heap to select most profitable affordable project\n");
            sb.append("3. Repeat until k projects completed or no more affordable projects\n");
            
            resultArea.setText(sb.toString());
            
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for all fields!");
        } catch (Exception ex) {
            showError("An error occurred: " + ex.getMessage());
        }
    }
    
    private void clearAll() {
        kField.setText("2");
        capitalField.setText("0");
        revenuesField.setText("2,5,8");
        investmentsField.setText("0,2,3");
        resultArea.setText("");
    }
    
    private void loadExample() {
        kField.setText("3");
        capitalField.setText("1");
        revenuesField.setText("3,6,10");
        investmentsField.setText("1,3,5");
        resultArea.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // List of all projects (investment, revenue)
        List<int[]> projects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            projects.add(new int[]{investments[i], revenues[i]});
        }

        // Sort projects by required investment (ascending)
        projects.sort(Comparator.comparingInt(a -> a[0]));

        // Max-heap to select the project with max revenue we can afford
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        int i = 0;
        for (int j = 0; j < k; j++) {
            // Add all affordable projects to the max-heap
            while (i < n && projects.get(i)[0] <= c) {
                maxHeap.offer(projects.get(i)[1]); // add revenue to max-heap
                i++;
            }

            // No more projects we can afford
            if (maxHeap.isEmpty()) {
                break;
            }

            // Take the most profitable project
            c += maxHeap.poll();
        }

        return c;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question1::new);
    }
}