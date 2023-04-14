package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable {
    public ArrayList<Employee> employees = new ArrayList<>();
    public ArrayList<Project> projects = new ArrayList<>();
    public ArrayList<Log> logs = new ArrayList<>();
}
