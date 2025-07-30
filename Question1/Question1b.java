// Scenario: Secure Bank PIN Policy Upgrade
// A bank is implementing a new PIN security policy to strengthen customer account protection. Every
// customer's banking PIN must meet the following criteria:
// 1. The PIN must be between 6 and 20 characters (inclusive).
// 2. It must contain at least one lowercase letter, one uppercase letter, and one digit.
// 3. It must not contain three consecutive repeating characters (e.g., "AAA123" is weak, but
// "AA123B" is strong).
// The bank wants to ensure all PINs comply with these security rules.
// Given a string pin_code, return the minimum number of changes required to make it strong. If the PIN
// is already strong, return 0.
// In one step, you can:
//  Insert a character.
//  Delete a character.
//  Replace one character with another character.
// Example 1:
// Input: pin_code = "X1!"
// Output: 3
// Explanation:
// Length is too short (3 characters, needs at least 6) → Insert 3 characters
// Missing a lowercase letter → Insert "a"
// Final strong PIN: "X1!abc"
// Example 2:
// Input: pin_code = "123456"
// Output: 2
// Explanation:
// Missing an uppercase letter → Replace "1" with "A"
// Missing a lowercase letter → Replace "2" with "b"
// Final strong PIN: "Ab3456"
// Example 3:
// Input: pin_code = "Aa1234!"
// Output: 0
// Explanation: Already meets all criteria

package Question1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Question1b extends JFrame {
    private JTextField pinField;
    private JTextArea resultArea;
    private JButton analyzeBtn, clearBtn, exampleBtn;
    private JPanel mainPanel, inputPanel, buttonPanel, resultPanel;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;
    
    public Question1b() {
        setTitle("🔐 Secure PIN Strength Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 700);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Input field with modern styling
        pinField = new JTextField(30);
        pinField.setFont(new Font("Consolas", Font.BOLD, 16));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Strength indicator
        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(true);
        strengthBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        strengthBar.setForeground(new Color(40, 167, 69));
        strengthBar.setBackground(new Color(248, 249, 250));
        
        strengthLabel = new JLabel("PIN Strength: ");
        strengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        strengthLabel.setForeground(new Color(33, 37, 41));
        
        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(248, 249, 250));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Buttons with modern styling
        analyzeBtn = createStyledButton("🔍 Analyze PIN Strength", new Color(40, 167, 69));
        clearBtn = createStyledButton("🗑️ Clear", new Color(220, 53, 69));
        exampleBtn = createStyledButton("📝 Load Examples", new Color(0, 123, 255));
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
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 248, 240), 
                                                         getWidth(), getHeight(), new Color(240, 248, 255));
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
        JLabel titleLabel = new JLabel("🔐 Secure PIN Strength Analyzer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Bank Security Policy Compliance Checker");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);
        
        // PIN input
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        inputPanel.add(createInputRow("Enter PIN Code:", pinField), gbc);
        
        // Strength indicator
        gbc.gridy = 3;
        JPanel strengthPanel = new JPanel(new BorderLayout(10, 0));
        strengthPanel.setOpaque(false);
        strengthPanel.add(strengthLabel, BorderLayout.WEST);
        strengthPanel.add(strengthBar, BorderLayout.CENTER);
        inputPanel.add(strengthPanel, gbc);
        
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
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
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
        // Add action listeners
        analyzeBtn.addActionListener(_ -> analyzePIN());
        clearBtn.addActionListener(_ -> clearAll());
        exampleBtn.addActionListener(_ -> loadExample());
        
        // Real-time analysis on text change
        pinField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrengthIndicator(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateStrengthIndicator(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateStrengthIndicator(); }
        });
        
        // Enter key support
        pinField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    analyzePIN();
                }
            }
        });
    }
    
    private void updateStrengthIndicator() {
        String pin = pinField.getText();
        if (pin.isEmpty()) {
            strengthBar.setValue(0);
            strengthBar.setString("Enter PIN");
            strengthBar.setForeground(new Color(108, 117, 125));
            return;
        }
        
        int strength = calculatePINStrength(pin);
        strengthBar.setValue(strength);
        
        if (strength >= 80) {
            strengthBar.setForeground(new Color(40, 167, 69));
            strengthBar.setString("Strong");
        } else if (strength >= 60) {
            strengthBar.setForeground(new Color(255, 193, 7));
            strengthBar.setString("Moderate");
        } else if (strength >= 40) {
            strengthBar.setForeground(new Color(253, 126, 20));
            strengthBar.setString("Weak");
        } else {
            strengthBar.setForeground(new Color(220, 53, 69));
            strengthBar.setString("Very Weak");
        }
    }
    
    private int calculatePINStrength(String pin) {
        int strength = 0;
        
        // Length check (0-30 points)
        if (pin.length() >= 6 && pin.length() <= 20) {
            strength += 30;
        } else if (pin.length() > 20) {
            strength += 20;
        } else {
            strength += (pin.length() * 5);
        }
        
        // Character type diversity (0-40 points)
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        for (char ch : pin.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;
        }
        
        int typeCount = (hasLower ? 1 : 0) + (hasUpper ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);
        strength += (typeCount * 10);
        
        // No consecutive repeating characters (0-30 points)
        int consecutiveRepeats = 0;
        for (int i = 2; i < pin.length(); i++) {
            if (pin.charAt(i) == pin.charAt(i-1) && pin.charAt(i) == pin.charAt(i-2)) {
                consecutiveRepeats++;
            }
        }
        strength += Math.max(0, 30 - (consecutiveRepeats * 10));
        
        return Math.min(100, strength);
    }
    
    private void analyzePIN() {
        String pin = pinField.getText().trim();
        
        if (pin.isEmpty()) {
            showError("Please enter a PIN code!");
            return;
        }
        
        int changes = strongPINChanges(pin);
        
        StringBuilder sb = new StringBuilder();
        sb.append("🔍 PIN ANALYSIS REPORT\n");
        sb.append("=====================\n\n");
        
        sb.append("📝 Input PIN: \"").append(pin).append("\"\n");
        sb.append("📏 Length: ").append(pin.length()).append(" characters\n\n");
        
        // Detailed analysis
        boolean hasLower = false, hasUpper = false, hasDigit = false;
        for (char ch : pin.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
        }
        
        sb.append("📋 CHARACTER ANALYSIS:\n");
        sb.append("• Lowercase letters: ").append(hasLower ? "✅ Present" : "❌ Missing").append("\n");
        sb.append("• Uppercase letters: ").append(hasUpper ? "✅ Present" : "❌ Missing").append("\n");
        sb.append("• Digits: ").append(hasDigit ? "✅ Present" : "❌ Missing").append("\n");
        sb.append("• Length requirement (6-20): ").append(
            pin.length() >= 6 && pin.length() <= 20 ? "✅ Met" : "❌ Not met").append("\n\n");
        
        // Consecutive repeating check
        int consecutiveRepeats = 0;
        for (int i = 2; i < pin.length(); i++) {
            if (pin.charAt(i) == pin.charAt(i-1) && pin.charAt(i) == pin.charAt(i-2)) {
                consecutiveRepeats++;
            }
        }
        sb.append("🔄 CONSECUTIVE CHARACTERS:\n");
        sb.append("• Consecutive repeating sequences: ").append(consecutiveRepeats).append("\n");
        sb.append("• Policy compliance: ").append(consecutiveRepeats == 0 ? "✅ Compliant" : "❌ Non-compliant").append("\n\n");
        
        sb.append("🎯 FINAL RESULT:\n");
        if (changes == 0) {
            sb.append("✅ PIN is already strong! No changes required.\n");
        } else {
            sb.append("⚠️  PIN needs ").append(changes).append(" change(s) to become strong.\n");
        }
        sb.append("\n");
        
        sb.append("💡 RECOMMENDATIONS:\n");
        if (!hasLower) sb.append("• Add at least one lowercase letter\n");
        if (!hasUpper) sb.append("• Add at least one uppercase letter\n");
        if (!hasDigit) sb.append("• Add at least one digit\n");
        if (pin.length() < 6) sb.append("• PIN is too short (minimum 6 characters)\n");
        if (pin.length() > 20) sb.append("• PIN is too long (maximum 20 characters)\n");
        if (consecutiveRepeats > 0) sb.append("• Avoid three consecutive repeating characters\n");
        
        resultArea.setText(sb.toString());
    }
    
    private void clearAll() {
        pinField.setText("");
        resultArea.setText("");
        strengthBar.setValue(0);
        strengthBar.setString("Enter PIN");
        strengthBar.setForeground(new Color(108, 117, 125));
    }
    
    private void loadExample() {
        String[] examples = {"X1!", "123456", "Aa1234!", "aaaa1111AAAA", "StrongP@ss123"};
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Choose an example PIN:",
            "Load Example",
            JOptionPane.QUESTION_MESSAGE,
            null,
            examples,
            examples[0]
        );
        
        if (selected != null) {
            pinField.setText(selected);
            analyzePIN();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int strongPINChanges(String pin_code) {
        int n = pin_code.length();

        // 1. Check for missing character types
        boolean hasLower = false, hasUpper = false, hasDigit = false;
        for (char ch : pin_code.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
        }
        int missingTypes = 0;
        if (!hasLower) missingTypes++;
        if (!hasUpper) missingTypes++;
        if (!hasDigit) missingTypes++;

        // 2. Count repeating sequences
        int replace = 0;
        int i = 2;
        while (i < n) {
            if (pin_code.charAt(i) == pin_code.charAt(i - 1) && pin_code.charAt(i) == pin_code.charAt(i - 2)) {
                int length = 2;
                while (i < n && pin_code.charAt(i) == pin_code.charAt(i - 1)) {
                    length++;
                    i++;
                }
                replace += length / 3;
            } else {
                i++;
            }
        }

        // 3. Handle length adjustments
        if (n < 6) {
            return Math.max(missingTypes, 6 - n);
        } else if (n <= 20) {
            return Math.max(missingTypes, replace);
        } else {
            int delete = n - 20;
            int remainingDelete = delete;

            // Optimize replacements using deletions
            i = 2;
            while (i < n && remainingDelete > 0) {
                if (pin_code.charAt(i) == pin_code.charAt(i - 1) && pin_code.charAt(i) == pin_code.charAt(i - 2)) {
                    int length = 3;
                    i++;
                    while (i < n && pin_code.charAt(i) == pin_code.charAt(i - 1)) {
                        length++;
                        i++;
                    }
                    int reduce = Math.min(remainingDelete, length - 2);
                    replace -= reduce / 3;
                    remainingDelete -= reduce;
                } else {
                    i++;
                }
            }

            return delete + Math.max(missingTypes, replace);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question1b());
    }
}
