package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.AppSettings;
import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

public class FillWithJunkData {
    public static void main(String[] args) throws InterruptedException {


        //Yeah det her er lidt scuffed men det bliver man nødt til for at den
        // følger med ProjectSettigns.remoteLocation.


        AppSettings.debugMode = false;
        AppSettings.init();

        new ClientTask<AppBackend>(appBackend -> appBackend.createEmployee("Thor"),Throwable::printStackTrace);

        Thread.sleep(500);
        LoginManager.attemptLogin("Thor");

        Session session = LoginManager.getCurrentSession();

        new ClientTask<AppBackend>(database -> {


            int frederik = database.createEmployee("Frederik");
            int katinka = database.createEmployee("Katinka");
            int jens = database.createEmployee("Jens");
            int karsten = database.createEmployee("Karsten");
            int christian = database.createEmployee("Christian");
            int tobias = database.createEmployee("Tobias");
            int obama = database.createEmployee("Obama");

            int byg = database.createProject("Byg et tårn" ,"", session);
            database.addEmployeeToProject(byg,"Frederik",session);
            database.addEmployeeToProject(byg,"Katinka",session);
            database.addEmployeeToProject(byg,"Obama",session);

            int kage = database.createProject("Bag en kage","", session);
            database.addEmployeeToProject(kage,"Christian",session);
            database.addEmployeeToProject(kage,"Katinka",session);

            int find = database.createActivity(byg,"Find en byggeplads", 1,session);

            database.addEmployeeToActivity(byg,find,"Katinka",session);
            database.addEmployeeToActivity(byg,find,"Thor",session);

        },Throwable::printStackTrace);
    }
}
