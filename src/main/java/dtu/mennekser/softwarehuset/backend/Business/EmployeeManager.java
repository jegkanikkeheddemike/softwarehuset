package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.Employee;

import java.util.ArrayList;

public class EmployeeManager {
    public static DataQuery<ArrayList<Employee>> getAllEmployees() {
        return database -> database.employees;
    }
}
