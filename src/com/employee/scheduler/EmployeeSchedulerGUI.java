package com.employee.scheduler;

import javax.swing.*;

import com.employee.scheduler.model.DayOfWeek;
import com.employee.scheduler.model.Employee;
import com.employee.scheduler.model.Shift;
import com.employee.scheduler.model.ShiftPreference;
import com.employee.scheduler.model.ShiftType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeSchedulerGUI extends JFrame {
	private JTextField nameField;
    private JComboBox<DayOfWeek> dayComboBox;
    private JComboBox<ShiftType> shiftComboBox;
    private JSpinner prioritySpinner;
    private JTextArea outputArea;
    private JButton addEmployeeButton;
    private JButton generateScheduleButton;

    private Scheduler scheduler;

    public EmployeeSchedulerGUI() {
        scheduler = new Scheduler();

        // Set up the main frame
        setTitle("Employee Scheduler");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Employee Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Day:"));
        dayComboBox = new JComboBox<>(DayOfWeek.values());
        inputPanel.add(dayComboBox);

        inputPanel.add(new JLabel("Shift:"));
        shiftComboBox = new JComboBox<>(ShiftType.values());
        inputPanel.add(shiftComboBox);

        inputPanel.add(new JLabel("Priority:"));
        prioritySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        inputPanel.add(prioritySpinner);

        addEmployeeButton = new JButton("Add Employee Preference");
        inputPanel.add(addEmployeeButton);

        generateScheduleButton = new JButton("Generate Schedule");
        inputPanel.add(generateScheduleButton);

        add(inputPanel, BorderLayout.NORTH);

        // Output panel
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployeePreference();
            }
        });

        generateScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSchedule();
            }
        });
    }

    private void addEmployeePreference() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an employee name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DayOfWeek day = (DayOfWeek) dayComboBox.getSelectedItem();
        ShiftType shiftType = (ShiftType) shiftComboBox.getSelectedItem();
        int priority = (int) prioritySpinner.getValue();

        Employee employee = findOrCreateEmployee(name);
        employee.addPreference(day, new ShiftPreference(shiftType, priority));

        outputArea.append("Added preference for " + name + ": " + day + " " + shiftType + " (Priority " + priority + ")\n");
    }

    private Employee findOrCreateEmployee(String name) {
        for (Employee employee : scheduler.getEmployees()) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        Employee newEmployee = new Employee(name);
        scheduler.addEmployee(newEmployee);
        return newEmployee;
    }

    private void generateSchedule() {
        scheduler.generateSchedule();
        outputArea.append("\nFinal Schedule:\n");

        for (DayOfWeek day : DayOfWeek.values()) {
            outputArea.append(day + ":\n");
            for (Shift shift : scheduler.getShifts().get(day)) {
                outputArea.append("  " + shift.getShiftType() + ": " + formatEmployeeList(shift.getAssignedEmployees()) + "\n");
            }
        }
    }

    private String formatEmployeeList(List<Employee> employees) {
        if (employees.isEmpty()) {
            return "No employees assigned";
        }
        StringBuilder sb = new StringBuilder();
        for (Employee employee : employees) {
            sb.append(employee.getName()).append(", ");
        }
        // Remove the trailing comma and space
        return sb.substring(0, sb.length() - 2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmployeeSchedulerGUI().setVisible(true);
            }
        });
    }
}
