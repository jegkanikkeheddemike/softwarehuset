package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.Table;

import java.io.Serializable;

public class Tables implements Serializable {
    public Table<Employee> users = new Table<>();
    public Table<Project> projects = new Table<>();
    public Table<Log> logs = new Table<>();
    public Table<Task> tasks = new Table<>();
}
