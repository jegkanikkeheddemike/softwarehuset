package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.schema.Employee;

import java.util.ArrayList;

public class EmployeeManager {
    public static Query<Database,ArrayList<Employee>> getAllEmployees() {
        assert LoginManager.getLoggedInEmployee() != null;

        return database -> database.employees;
    }
}
