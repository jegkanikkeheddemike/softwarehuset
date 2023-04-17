package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.OnceQuery;
import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.Employee;

public class LoginManager {

    private static Employee loggedInEmployee;
    public static Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public static void attemptLogin(String username) {
        loggedInEmployee = new OnceQuery<Employee>(database -> database.findEmployee(username)).fetch();
    }
    public static void logout() {
        loggedInEmployee = null;
    }
}
