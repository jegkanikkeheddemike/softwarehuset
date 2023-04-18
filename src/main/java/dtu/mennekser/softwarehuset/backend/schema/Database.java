package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Database extends DataLayer{

    public ArrayList<Employee> employees = new ArrayList<>();
    public ArrayList<Project> projects = new ArrayList<>();




    //Den her synes jeg stadig bruges meget underligt. Se klassediagrammet
    public int createProject(String name , int employeeID) {
        projects.add(new Project(name, projects.size()));

        //automatically assigns the Employee that creates the project
        projects.get(projects.size()-1).assignEmployee(employeeID);
        return projects.size()-1;
    }

    public int createEmployee(String name) {
        employees.add(new Employee(name, employees.size()));
        return employees.size()-1;
    }
    public Employee findEmployee(String name) {
        for (Employee employee : employees) {
            if (employee.name.equals(name)) {
                return employee;
            }
        }
        return null;
    }
    public Project findProject(String name) {
        for (Project project : projects) {
            if (project.name.equals(name)) {
                return project;
            }
        }
        return null;
    }
}
