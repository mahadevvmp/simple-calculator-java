package simplecalculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private double firstNumber;
    private String operator;
    private boolean startNewNumber;

    public Calculator() {
        firstNumber = 0;
        operator = "";
        startNewNumber = true;

        setTitle("Simple Calculator");
        setSize(360, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] labels = {
            "C", "÷", "×", "−",
            "7", "8", "9", "+",
            "4", "5", "6", "=",
            "1", "2", "3", "",
            "0", ".", "⌫", ""
        };

        for (String label : labels) {
            if (label.isEmpty()) {
                buttonPanel.add(new JPanel());
            } else {
                JButton button = new JButton(label);
                button.setFont(new Font("Arial", Font.BOLD, 22));
                button.setFocusPainted(false);
                button.addActionListener(this);
                buttonPanel.add(button);
            }
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.matches("[0-9]")) {
            addDigit(command);
        } else if (command.equals(".")) {
            addDecimalPoint();
        } else if (command.equals("C")) {
            clear();
        } else if (command.equals("⌫")) {
            deleteLastCharacter();
        } else if (command.equals("+") || command.equals("−")
                || command.equals("×") || command.equals("÷")) {
            chooseOperator(command);
        } else if (command.equals("=")) {
            calculateResult();
        }
    }

    private void addDigit(String digit) {
        if (startNewNumber || display.getText().equals("0")
                || display.getText().equals("Error")) {
            display.setText(digit);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + digit);
        }
    }

    private void addDecimalPoint() {
        if (startNewNumber || display.getText().equals("Error")) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void chooseOperator(String selectedOperator) {
        if (!operator.isEmpty() && !startNewNumber) {
            calculateResult();
        }

        if (!display.getText().equals("Error")) {
            firstNumber = Double.parseDouble(display.getText());
            operator = selectedOperator;
            startNewNumber = true;
        }
    }

    private void calculateResult() {
        if (operator.isEmpty() || startNewNumber
                || display.getText().equals("Error")) {
            return;
        }

        double secondNumber = Double.parseDouble(display.getText());
        double result;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "−":
                result = firstNumber - secondNumber;
                break;
            case "×":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber == 0) {
                    display.setText("Error");
                    operator = "";
                    startNewNumber = true;
                    return;
                }
                result = firstNumber / secondNumber;
                break;
            default:
                return;
        }

        display.setText(formatResult(result));
        operator = "";
        startNewNumber = true;
    }

    private String formatResult(double result) {
        if (result == Math.rint(result)) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }

    private void deleteLastCharacter() {
        String currentText = display.getText();

        if (startNewNumber || currentText.equals("Error")) {
            return;
        }

        if (currentText.length() == 1) {
            display.setText("0");
            startNewNumber = true;
        } else {
            display.setText(currentText.substring(0,
                    currentText.length() - 1));
        }
    }

    private void clear() {
        firstNumber = 0;
        operator = "";
        startNewNumber = true;
        display.setText("0");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }
}