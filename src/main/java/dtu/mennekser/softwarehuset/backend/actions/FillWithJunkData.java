package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.AppSettings;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

public class FillWithJunkData {
    public static void main(String[] args) {


        //Yeah det her er lidt scuffed men det bliver man nødt til for at den
        // følger med ProjectSettigns.remoteLocation.


        AppSettings.debugMode = false;
        AppSettings.init();

        new ClientTask<Database>(database -> {
            int thor = database.createEmployee("Thor");
            int frederik = database.createEmployee("Frederik");
            int katinka = database.createEmployee("Katinka");
            int jens = database.createEmployee("Jens");
            int karsten = database.createEmployee("Karsten");
            int christan = database.createEmployee("Christan");
            int tobias = database.createEmployee("Tobias");
            int obama = database.createEmployee("Obama");

            int byg = database.createProject("Byg et tårn" , thor);
            database.projects.get(byg).assignEmployee(frederik);
            database.projects.get(byg).assignEmployee(katinka);
            database.projects.get(byg).assignEmployee(obama);

            int kage = database.createProject("Bag en kage", katinka);
            database.projects.get(kage).assignEmployee(christan);
            database.projects.get(kage).assignEmployee(thor);

            int byggeplads = database.projects.get(byg).createActivity("Find en byggeplads", 30);
            database.projects.get(byg).activities.get(byggeplads).assignEmployee(thor);
            database.projects.get(byg).activities.get(byggeplads).assignEmployee(katinka);
        },Throwable::printStackTrace);
    }
}
