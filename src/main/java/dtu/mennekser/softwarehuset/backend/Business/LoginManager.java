package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.OnceQuery;
import dtu.mennekser.softwarehuset.backend.schema.Employee;

public class LoginManager {

    private static Employee loggedInEmployee;
    public static Employee getLoggedInEmployee(){
        //probably needs to throw a "No employee logged in" error,
        //but then 17 problem that will have to be fixed occurs :D
        return loggedInEmployee;
    }
    public static void setLoggedInEmployee(Employee employee){
        loggedInEmployee = employee;
    }

    public static void attemptLogin(String username) {
        assert loggedInEmployee == null;
        loggedInEmployee = new OnceQuery<>(database -> database.findEmployee(username)).fetch();
    }
    public static void logout() {
        assert loggedInEmployee != null;
        loggedInEmployee = null;
    }
}
