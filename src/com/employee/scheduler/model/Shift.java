package com.employee.scheduler.model;

import java.util.*;

public class Shift {
    private DayOfWeek day;
    private ShiftType shiftType;
    private List<Employee> assignedEmployees;

    public Shift(DayOfWeek day, ShiftType shiftType) {
        this.day = day;
        this.shiftType = shiftType;
        this.assignedEmployees = new ArrayList<>();
    }

    public DayOfWeek getDay() {
        return day;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void assignEmployee(Employee employee) {
        assignedEmployees.add(employee);
    }

    public boolean isEmployeeAssigned(Employee employee) {
        return assignedEmployees.contains(employee);
    }
}