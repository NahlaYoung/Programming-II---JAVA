import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Joshua Baca's work: Class definition and main method setup, architect, fixing bugs and errors, assisting group members with code
// Main class extending JFrame to create a GUI application
public class Calculator extends JFrame {
    private final JTextField display = new JTextField(); // Display area for input and results
    private boolean isNewOperation = true;  // Flag to track whether the next input starts a new operation

    // George Martin Salazar's work: GUI Layout and Frame Setup, testing program, assisting group members with code
    // Constructor that initializes the GUI components
    public Calculator() {
        setTitle("Calculator"); // Set the window's title
        setLayout(new BorderLayout()); // Use BorderLayout for arranging components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the window is closed
        setSize(300, 400); // Set the size of the calculator window
        createCalculatorGUI();
        setVisible(true); // Make the window visible
    }

    // Safria George's work: Display and Button Panel Setup
    // Sets up the calculator's GUI components
    private void createCalculatorGUI() {
        display.setFont(new Font("Arial", Font.BOLD, 24)); // Set the font for the display
        display.setEditable(false); // Disable editing of the display directly
        add(display, BorderLayout.NORTH); // Add the display at the top of the layout
        setupButtonPanel();
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(); // Panel to hold the buttons
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5)); // Grid layout for buttons
        String[] buttons = {"7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "*", "0", "=", "C", "/"};
        // Loop through each button label to create and add buttons
        for (String b : buttons) {
            JButton button = new JButton(b);
            button.setFont(new Font("Arial", Font.BOLD, 18)); // Set the font of the button
            button.addActionListener(new ButtonClickListener()); // Add action listeners for button clicks
            buttonPanel.add(button); // Add each button to the panel
        }
        add(buttonPanel, BorderLayout.CENTER); // Place the button panel in the center of the layout
    }

    // Nahla Young's work: Button Click Event Handling
    // Inner class to handle button click events
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand(); // Get the command from the button
            processCommand(command);
        }

        private void processCommand(String command) {
            // Number handling
            if (command.matches("[0-9]")) { // Check if the command is a number
                if (isNewOperation) {
                    display.setText(command); // Start a new number if it's a new operation
                    isNewOperation = false;
                } else {
                    display.setText(display.getText() + command); // Append to the existing number
                }
            }
            // Operator handling
            else if (command.matches("[+\\-*/]")) { // Check if the command is an operator
                if (!isNewOperation) {
                    display.setText(display.getText() + command); // Append operator if it's part of an ongoing operation
                }
            }
            // Equals handling
            else if (command.equals("=")) {
                if (validateExpression(display.getText())) { // Validate the expression before calculating
                    try {
                        calculateAndDisplayResult(); // Calculate and display the result
                        isNewOperation = true;
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(Calculator.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        display.setText("");
                        isNewOperation = true;
                    }
                } else {
                    JOptionPane.showMessageDialog(Calculator.this,
                            "Invalid operation. This calculator cannot handle mixed operations without proper order of operations.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Clear handling
            else if (command.equals("C")) {
                display.setText(""); // Clear the display
                isNewOperation = true; // Reset the operation flag
            }
        }
    }

    // Moises Orozco's work: Expression Validation and Calculation Logic
    // Validates that the expression is suitable for left-to-right calculation
    private boolean validateExpression(String expression) {
        return !(expression.contains("+") || expression.contains("-")) || !(expression.contains("*") || expression.contains("/"));
    }

    // Calculates and displays the result based on the current expression in the display
    private void calculateAndDisplayResult() {
        String expression = display.getText();
        double result = evaluateExpression(expression);
        display.setText(String.valueOf(result));
    }

    // Evaluates mathematical expressions
    private double evaluateExpression(String expression) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        java.util.List<Character> operators = new java.util.ArrayList<>();
        int lastSplitIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                parts.add(expression.substring(lastSplitIndex, i));
                operators.add(ch);
                lastSplitIndex = i + 1;
            }
        }
        parts.add(expression.substring(lastSplitIndex));

        double result = Double.parseDouble(parts.getFirst());
        for (int i = 0; i < operators.size(); i++) {
            double nextOperand = Double.parseDouble(parts.get(i + 1));
            switch (operators.get(i)) {
                case '+':
                    result += nextOperand;
                    break;
                case '-':
                    result -= nextOperand;
                    break;
                case '*':
                    result *= nextOperand;
                    break;
                case '/':
                    if (nextOperand == 0) {
                        throw new IllegalArgumentException("Cannot divide by zero.");
                    }
                    result /= nextOperand;
                    break;
            }
        }
        return result;
    }

    // Person 1's work: Launching the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}


/*
Group - 4
Joshua Baca, Safria George, Moises Orozco, George Martin Salazar, Nahla Young
Basic Calculator Group Project
IS-2063-006 - Programing Language 2 With Java
University of Texas at San Antonio
*/