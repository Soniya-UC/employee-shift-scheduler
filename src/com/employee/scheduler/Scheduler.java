package com.employee.scheduler;

import java.util.*;

import com.employee.scheduler.model.DayOfWeek;
import com.employee.scheduler.model.Employee;
import com.employee.scheduler.model.Shift;
import com.employee.scheduler.model.ShiftPreference;
import com.employee.scheduler.model.ShiftType;

public class Scheduler {
    private List<Employee> employees;
    private Map<DayOfWeek, List<Shift>> shifts;

    public Scheduler() {
        this.employees = new ArrayList<>();
        this.shifts = new HashMap<>();
        initializeShifts();
    }

    private void initializeShifts() {
        for (DayOfWeek day : DayOfWeek.values()) {
            List<Shift> dayShifts = new ArrayList<>();
            for (ShiftType shiftType : ShiftType.values()) {
                dayShifts.add(new Shift(day, shiftType));
            }
            shifts.put(day, dayShifts);
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void generateSchedule() {
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : shifts.get(day)) {
                assignEmployeesToShift(shift, day);
            }
        }
    }

    private void assignEmployeesToShift(Shift shift, DayOfWeek day) {
        // Assign employees based on preferences
        for (Employee employee : employees) {
            if (employee.getDaysWorked() >= 5) continue; // Skip if employee has worked 5 days
            if (isEmployeeAssignedToDay(employee, day)) continue; // Skip if employee is already assigned to a shift on this day

            List<ShiftPreference> preferences = employee.getPreferences().get(day);
            if (preferences != null) {
                // Sort preferences by priority (ascending order)
                preferences.sort(Comparator.comparingInt(ShiftPreference::getPriority));

                for (ShiftPreference preference : preferences) {
                    if (preference.getShiftType() == shift.getShiftType() && shift.getAssignedEmployees().size() < 2) {
                        shift.assignEmployee(employee);
                        employee.incrementDaysWorked();
                        break; // Assign to the highest priority shift available
                    }
                }
            }
        }

        // Ensure at least 2 employees per shift
        while (shift.getAssignedEmployees().size() < 2) {
            Employee randomEmployee = getRandomAvailableEmployee(day);
            if (randomEmployee != null) {
                shift.assignEmployee(randomEmployee);
                randomEmployee.incrementDaysWorked();
            } else {
                break; // No more available employees
            }
        }
    }

    private boolean isEmployeeAssignedToDay(Employee employee, DayOfWeek day) {
        for (Shift shift : shifts.get(day)) {
            if (shift.getAssignedEmployees().contains(employee)) {
                return true;
            }
        }
        return false;
    }

    private Employee getRandomAvailableEmployee(DayOfWeek day) {
        List<Employee> availableEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getDaysWorked() < 5 && !isEmployeeAssignedToDay(employee, day)) {
                availableEmployees.add(employee);
            }
        }
        if (availableEmployees.isEmpty()) return null;
        return availableEmployees.get(new Random().nextInt(availableEmployees.size()));
    }

    public Map<DayOfWeek, List<Shift>> getShifts() {
        return shifts;
    }

}