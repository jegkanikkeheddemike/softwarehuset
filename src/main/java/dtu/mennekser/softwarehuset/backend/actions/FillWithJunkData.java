package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.backend.db.Employee;

public class FillWithJunkData {
    public static void main(String[] args) {
        DBTask.ExecuteTask(database -> {
            int thor = database.createEmployee("Thor");
            int frederik = database.createEmployee("Frederik");
            int katinka = database.createEmployee("Katinka");
            int jens = database.createEmployee("Jens");
            int karsten = database.createEmployee("Karsten");
            int christan = database.createEmployee("Christan");
            int tobias = database.createEmployee("Tobias");
            int obama = database.createEmployee("Obama");

            int tårn = database.createProject("Byg et tårn");
            database.projects.get(tårn).assignEmployee(thor);

        });
    }
}
