// Cryptarithmetic Puzzle
// Imagine a puzzle where words represent numbers, and we need to find a unique digit mapping for each
// letter to satisfy a given equation. The rule is that no two letters can have the same digit, and numbers
// must not have leading zeros.
// Write a function that takes in three strings: word1, word2, and result.
// Return true if there exists a valid one-to-one digit mapping such that:
// numeric_value(word1) + numeric_value(word2) == numeric_value(result)
// Example 1:
// Input: word1 = "STAR", word2 = "MOON", result = "NIGHT"
// Output: true
// Example 2:
// Input: word1 = "CODE", word2 = "BUG", result = "DEBUG"
// Output: false

package Question2;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Question2b extends JFrame {
    private JTextField word1Field, word2Field, resultField;
    private JTextArea resultArea;
    private JButton solveBtn, clearBtn, exampleBtn;
    private JPanel mainPanel, inputPanel, buttonPanel, resultPanel;
    
    public Question2b() {
        setTitle("🧩 Cryptarithmetic Puzzle Solver");
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
        word1Field = createStyledTextField("STAR");
        word2Field = createStyledTextField("MOON");
        resultField = createStyledTextField("NIGHT");
        
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
        solveBtn = createStyledButton("🧩 Solve Puzzle", new Color(40, 167, 69));
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
        JLabel titleLabel = new JLabel("🧩 Cryptarithmetic Puzzle Solver");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Word-to-Number Equation Solver");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        gbc.gridy = 1;
        inputPanel.add(subtitleLabel, gbc);
        
        // Input fields
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        inputPanel.add(createInputRow("Word 1:", word1Field), gbc);
        gbc.gridy = 3;
        inputPanel.add(createInputRow("Word 2:", word2Field), gbc);
        gbc.gridy = 4;
        inputPanel.add(createInputRow("Result:", resultField), gbc);
        
        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(solveBtn);
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
        solveBtn.addActionListener(_ -> solvePuzzle());
        clearBtn.addActionListener(_ -> clearAll());
        exampleBtn.addActionListener(_ -> loadExamples());
        
        // Enter key support
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    solvePuzzle();
                }
            }
        };
        
        word1Field.addKeyListener(enterKey);
        word2Field.addKeyListener(enterKey);
        resultField.addKeyListener(enterKey);
    }
    
    private void solvePuzzle() {
        String word1 = word1Field.getText().trim().toUpperCase();
        String word2 = word2Field.getText().trim().toUpperCase();
        String result = resultField.getText().trim().toUpperCase();
        
        if (word1.isEmpty() || word2.isEmpty() || result.isEmpty()) {
            showError("Please enter all three words!");
            return;
        }
        
        boolean isSolvable = isSolvable(word1, word2, result);
        
        StringBuilder sb = new StringBuilder();
        sb.append("🧩 CRYPTARITHMETIC PUZZLE ANALYSIS\n");
        sb.append("==================================\n\n");
        
        sb.append("📝 INPUT EQUATION:\n");
        sb.append("• Word 1: \"").append(word1).append("\"\n");
        sb.append("• Word 2: \"").append(word2).append("\"\n");
        sb.append("• Result: \"").append(result).append("\"\n");
        sb.append("• Equation: ").append(word1).append(" + ").append(word2).append(" = ").append(result).append("\n\n");
        
        // Analyze unique letters
        Set<Character> uniqueLetters = new HashSet<>();
        for (char c : word1.toCharArray()) uniqueLetters.add(c);
        for (char c : word2.toCharArray()) uniqueLetters.add(c);
        for (char c : result.toCharArray()) uniqueLetters.add(c);
        
        sb.append("🔤 CHARACTER ANALYSIS:\n");
        sb.append("• Unique letters: ").append(uniqueLetters.size()).append("\n");
        sb.append("• Letters: ").append(uniqueLetters.toString()).append("\n");
        sb.append("• Maximum allowed: 10 (digits 0-9)\n");
        sb.append("• Status: ").append(uniqueLetters.size() <= 10 ? "✅ Valid" : "❌ Too many letters").append("\n\n");
        
        sb.append("🎯 SOLUTION RESULT:\n");
        if (isSolvable) {
            sb.append("✅ SOLVABLE: A valid digit mapping exists!\n\n");
            sb.append("💡 EXPLANATION:\n");
            sb.append("• The puzzle can be solved with unique digit assignments\n");
            sb.append("• No leading zeros are assigned to any word\n");
            sb.append("• The equation holds true with the assigned digits\n");
        } else {
            sb.append("❌ UNSOLVABLE: No valid digit mapping exists.\n\n");
            sb.append("💡 EXPLANATION:\n");
            sb.append("• No combination of unique digits satisfies the equation\n");
            sb.append("• This could be due to mathematical impossibility\n");
            sb.append("• Or constraints like leading zeros or digit conflicts\n");
        }
        sb.append("\n");
        
        sb.append("🔧 ALGORITHM EXPLANATION:\n");
        sb.append("1. Use backtracking to try all possible digit assignments\n");
        sb.append("2. Ensure each letter gets a unique digit (0-9)\n");
        sb.append("3. Prevent leading zeros in any word\n");
        sb.append("4. Check if the equation holds: word1 + word2 = result\n");
        
        resultArea.setText(sb.toString());
    }
    
    private void clearAll() {
        word1Field.setText("STAR");
        word2Field.setText("MOON");
        resultField.setText("NIGHT");
        resultArea.setText("");
    }
    
    private void loadExamples() {
        String[] examples = {
            "Example 1: STAR + MOON = NIGHT (Solvable)",
            "Example 2: CODE + BUG = DEBUG (Unsolvable)",
            "Example 3: SEND + MORE = MONEY (Classic)"
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
                word1Field.setText("STAR");
                word2Field.setText("MOON");
                resultField.setText("NIGHT");
            } else if (selected.contains("Example 2")) {
                word1Field.setText("CODE");
                word2Field.setText("BUG");
                resultField.setText("DEBUG");
            } else if (selected.contains("Example 3")) {
                word1Field.setText("SEND");
                word2Field.setText("MORE");
                resultField.setText("MONEY");
            }
            solvePuzzle();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // Main function as required by PDF
    public static boolean isSolvable(String word1, String word2, String result) {
        Set<Character> uniqueLetters = new HashSet<>();
        for (char c : word1.toCharArray()) uniqueLetters.add(c);
        for (char c : word2.toCharArray()) uniqueLetters.add(c);
        for (char c : result.toCharArray()) uniqueLetters.add(c);
        
        if (uniqueLetters.size() > 10) return false;
        
        java.util.List<Character> letters = new ArrayList<>(uniqueLetters);
        Map<Character, Integer> charToDigit = new HashMap<>();
        boolean[] usedDigits = new boolean[10];
        
        return solve(0, letters, charToDigit, usedDigits, word1, word2, result);
    }
    
    private static boolean solve(int index, java.util.List<Character> letters, Map<Character, Integer> charToDigit, 
                                boolean[] usedDigits, String word1, String word2, String result) {
        if (index == letters.size()) {
            return checkSolution(charToDigit, word1, word2, result);
        }

        char c = letters.get(index);
        for (int d = 0; d <= 9; d++) {
            if (!usedDigits[d]) {
                // Leading letter cannot be zero
                if (d == 0 && isLeadingLetter(c, word1, word2, result)) continue;

                usedDigits[d] = true;
                charToDigit.put(c, d);

                if (solve(index + 1, letters, charToDigit, usedDigits, word1, word2, result)) {
                    return true;
                }

                // Backtrack
                usedDigits[d] = false;
                charToDigit.remove(c);
            }
        }
        return false;
    }
    
    private static boolean isLeadingLetter(char c, String word1, String word2, String result) {
        return word1.charAt(0) == c || word2.charAt(0) == c || result.charAt(0) == c;
    }
    
    private static int wordToNumber(String w, Map<Character, Integer> charToDigit) {
        int num = 0;
        for (char c : w.toCharArray()) {
            num = num * 10 + charToDigit.get(c);
        }
        return num;
    }
    
    private static boolean checkSolution(Map<Character, Integer> charToDigit, String word1, String word2, String result) {
        int sum = wordToNumber(word1, charToDigit) + wordToNumber(word2, charToDigit);
        return sum == wordToNumber(result, charToDigit);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question2b::new);
    }
}
