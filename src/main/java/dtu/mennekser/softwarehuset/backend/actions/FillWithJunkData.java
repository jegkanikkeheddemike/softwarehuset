package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

public class FillWithJunkData {
    public static void main(String[] args) {
        DBTask.SubmitTask(database -> {
            int thor = database.createEmployee("Thor");
            int frederik = database.createEmployee("Frederik");
            int katinka = database.createEmployee("Katinka");
            int jens = database.createEmployee("Jens");
            int karsten = database.createEmployee("Karsten");
            int christan = database.createEmployee("Christan");
            int tobias = database.createEmployee("Tobias");
            int obama = database.createEmployee("Obama");

            int byg = database.createProject("Byg et tårn");
            database.projects.get(byg).assignEmployee(thor);
            database.projects.get(byg).assignEmployee(frederik);
            database.projects.get(byg).assignEmployee(karsten);
            database.projects.get(byg).assignEmployee(jens);
            database.projects.get(byg).assignEmployee(katinka);
            database.projects.get(byg).assignEmployee(obama);

            int kage = database.createProject("Bag en kage");
            database.projects.get(kage).assignEmployee(katinka);
            database.projects.get(kage).assignEmployee(christan);
            database.projects.get(kage).assignEmployee(tobias);
            database.projects.get(kage).assignEmployee(obama);
            database.projects.get(byg).assignEmployee(thor);

            int byggeplads = database.projects.get(byg).createActivity("Find en byggeplads", 30);
            database.projects.get(byg).activities.get(byggeplads).assignEmployee(thor);
            database.projects.get(byg).activities.get(byggeplads).assignEmployee(katinka);



        },Throwable::printStackTrace);
    }
}
