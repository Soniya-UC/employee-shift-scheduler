import tkinter as tk
from tkinter import messagebox, ttk
import json
import random

# Days of the week
DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

# Shift options
SHIFTS = ["Morning", "Afternoon", "Evening"]

# Maximum number of employees per shift per day
MAX_EMPLOYEES_PER_SHIFT = 2

# Global variables
schedule = {}
employees = []

# Function to add an employee
def add_employee():
    name = entry_name.get()
    if name in employees:
        messagebox.showwarning("Warning", "Employee already exists!")
    else:
        employees.append(name)
        schedule[name] = {
            "shifts": {day: None for day in DAYS},
            "preferences": {}
        }
        entry_name.delete(0, tk.END)
        update_employee_list()
        messagebox.showinfo("Success", f"Employee {name} added successfully!")

# Function to update the employee list in the GUI
def update_employee_list():
    listbox_employees.delete(0, tk.END)
    for employee in employees:
        listbox_employees.insert(tk.END, employee)

# Function to collect shift preferences for an employee
def collect_preferences():
    selected_employee = listbox_employees.get(tk.ACTIVE)
    if not selected_employee:
        messagebox.showwarning("Warning", "Please select an employee!")
        return

    preferences_window = tk.Toplevel(root)
    preferences_window.title(f"Shift Preferences for {selected_employee}")

    # Create preference widgets for each day
    for i, day in enumerate(DAYS):
        tk.Label(preferences_window, text=day).grid(row=i, column=0, padx=10, pady=5)
        for j, shift in enumerate(SHIFTS):
            tk.Label(preferences_window, text=shift).grid(row=i, column=j+1, padx=10, pady=5)
            entry = tk.Entry(preferences_window, width=5)
            entry.grid(row=i, column=j+2, padx=10, pady=5)
            schedule[selected_employee]["preferences"][(day, shift)] = entry

    # Save preferences button
    tk.Button(preferences_window, text="Save Preferences", command=lambda: save_preferences(selected_employee, preferences_window)).grid(row=len(DAYS), columnspan=4, pady=10)

# Function to save shift preferences
def save_preferences(employee, window):
    for day in DAYS:
        for shift in SHIFTS:
            entry = schedule[employee]["preferences"][(day, shift)]
            try:
                rank = int(entry.get())
                schedule[employee]["preferences"][(day, shift)] = rank
            except ValueError:
                schedule[employee]["preferences"][(day, shift)] = float('inf')  # Default to lowest priority
    messagebox.showinfo("Success", f"Preferences saved for {employee}!")
    window.destroy()

# Function to assign shifts based on preferences
def assign_shifts():
    # Reset all shifts to None at the start of assignment
    for name in schedule:
        for day in DAYS:
            schedule[name]["shifts"][day] = None

    # Assign shifts for each day
    for day in DAYS:
        for shift in SHIFTS:
            employees_assigned = 0

            # Assign at least 2 employees per shift
            while employees_assigned < MAX_EMPLOYEES_PER_SHIFT:
                # Find employees who are not already working on this day and have worked less than 5 days
                available_employees = [
                    name for name in schedule
                    if schedule[name]["shifts"][day] is None and count_shifts(name) < 5
                ]

                if not available_employees:
                    break

                # Sort available employees by their preference for this shift on this day
                available_employees.sort(key=lambda name: schedule[name]["preferences"].get((day, shift), float('inf')))

                # Assign the shift to the employee with the highest preference
                selected_employee = available_employees[0]
                schedule[selected_employee]["shifts"][day] = shift
                employees_assigned += 1

    # Display the final schedule
    display_schedule()

# Function to count the number of shifts an employee has in a week
def count_shifts(name):
    return sum(1 for day in DAYS if schedule[name]["shifts"][day] is not None)

# Function to display the final schedule
def display_schedule():
    schedule_window = tk.Toplevel(root)
    schedule_window.title("Final Schedule")

    # Create a treeview widget to display the schedule
    tree = ttk.Treeview(schedule_window, columns=("Employee", *DAYS), show="headings")
    tree.heading("Employee", text="Employee")
    for day in DAYS:
        tree.heading(day, text=day)
    tree.pack(padx=10, pady=10)

    # Populate the treeview with the schedule
    for name in schedule:
        shifts = [schedule[name]["shifts"][day] or "None" for day in DAYS]
        tree.insert("", tk.END, values=(name, *shifts))

# Main GUI
root = tk.Tk()
root.title("Employee Shift Scheduler")

# Employee name input
tk.Label(root, text="Employee Name:").grid(row=0, column=0, padx=10, pady=10)
entry_name = tk.Entry(root)
entry_name.grid(row=0, column=1, padx=10, pady=10)

# Add employee button
tk.Button(root, text="Add Employee", command=add_employee).grid(row=0, column=2, padx=10, pady=10)

# Employee list
tk.Label(root, text="Employees:").grid(row=1, column=0, padx=10, pady=10)
listbox_employees = tk.Listbox(root)
listbox_employees.grid(row=1, column=1, columnspan=2, padx=10, pady=10)

# Collect preferences button
tk.Button(root, text="Collect Preferences", command=collect_preferences).grid(row=2, column=0, padx=10, pady=10)
# Assign shift button
tk.Button(root, text="Assign Shifts", command=assign_shifts).grid(row=2, column=1, padx=10, pady=10)
# Display schedule button
tk.Button(root, text="Display Schedule", command=display_schedule).grid(row=2, column=2, padx=10, pady=10)

root.mainloop()