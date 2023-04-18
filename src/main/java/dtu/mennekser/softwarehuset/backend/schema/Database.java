package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Database extends DataLayer{

    public ArrayList<Employee> employees = new ArrayList<>();
    public ArrayList<Project> projects = new ArrayList<>();




    //Den her synes jeg stadig bruges meeeeget underligt. Se klassediagrammet
    public int createProject(String name) {
        projects.add(new Project(name, projects.size()));
        return projects.size()-1;
    }
    public Employee findEmployee(String name) {
        for (Employee employee : employees) {
            if (employee.name.equals(name)) {
                return employee;
            }
        }
        return null;
    }
    public int createEmployee(String name) {
        employees.add(new Employee(name, employees.size()));
        return employees.size()-1;
    }
}
