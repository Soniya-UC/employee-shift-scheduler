package com.employee.scheduler.model;

public class ShiftPreference {
	private ShiftType shiftType;
    private int priority; // 1 = highest priority, 3 = lowest

    public ShiftPreference(ShiftType shiftType, int priority) {
        this.shiftType = shiftType;
        this.priority = priority;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public int getPriority() {
        return priority;
    }
}
