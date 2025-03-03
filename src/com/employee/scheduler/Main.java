package com.employee.scheduler;

import java.util.*;

import com.employee.scheduler.model.DayOfWeek;
import com.employee.scheduler.model.Employee;
import com.employee.scheduler.model.Shift;
import com.employee.scheduler.model.ShiftPreference;
import com.employee.scheduler.model.ShiftType;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scheduler scheduler = new Scheduler();

        System.out.println("Enter the number of employees:");
        int numEmployees = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numEmployees; i++) {
            System.out.println("Enter employee name:");
            String name = scanner.nextLine();

            Employee employee = new Employee(name);

            System.out.println("Shift preferences for " + name + " (format: DAY SHIFT PRIORITY, e.g., MONDAY MORNING 1):");
            for (DayOfWeek day : DayOfWeek.values()) {
                System.out.println("Enter preferences for " + day + " (enter 'done' to finish):");
                while (true) {
                    System.out.println("Enter preference (format: SHIFT PRIORITY, e.g., MORNING 1):");
                    String input = scanner.nextLine().toUpperCase();
                    if (input.equalsIgnoreCase("done")) {
                        break;
                    }

                    String[] parts = input.split(" ");
                    if (parts.length != 2) {
                        System.out.println("Invalid input. Please try again.");
                        continue;
                    }

                    try {
                        ShiftType shiftType = ShiftType.valueOf(parts[0]);
                        int priority = Integer.parseInt(parts[1]);
                        employee.addPreference(day, new ShiftPreference(shiftType, priority));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
            }

            scheduler.addEmployee(employee);
        }

        // Generate schedule
        scheduler.generateSchedule();

        // Output schedule in a readable format
        System.out.println("\nFinal Schedule:");
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day + ":");
            for (Shift shift : scheduler.getShifts().get(day)) {
                System.out.println("  " + shift.getShiftType() + ": " + formatEmployeeList(shift.getAssignedEmployees()));
            }
        }

        scanner.close();
    }

    private static String formatEmployeeList(List<Employee> employees) {
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
}