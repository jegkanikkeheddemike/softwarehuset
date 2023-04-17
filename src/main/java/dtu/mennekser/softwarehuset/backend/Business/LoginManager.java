package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.Employee;

public class LoginManager {
    public static DataQuery<Employee> attempLogin(String username) {
        return database -> database.findEmployee(username);
    }
}
