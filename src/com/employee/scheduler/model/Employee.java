package com.employee.scheduler.model;

import java.util.*;

public class Employee {
    private String name;
    private Map<DayOfWeek, List<ShiftPreference>> preferences; // Store multiple preferences per day
    private int daysWorked;

    public Employee(String name) {
        this.name = name;
        this.preferences = new HashMap<>();
        this.daysWorked = 0;
    }

    public String getName() {
        return name;
    }

    public Map<DayOfWeek, List<ShiftPreference>> getPreferences() {
        return preferences;
    }

    public void addPreference(DayOfWeek day, ShiftPreference preference) {
        preferences.computeIfAbsent(day, k -> new ArrayList<>()).add(preference);
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public void incrementDaysWorked() {
        daysWorked++;
    }

    @Override
    public String toString() {
        return name;
    }
}