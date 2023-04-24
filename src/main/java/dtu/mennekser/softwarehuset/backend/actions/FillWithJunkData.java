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
        LoginManager.attemptLogin("thor");

        Session session = LoginManager.getCurrentSession();

        new ClientTask<AppBackend>(database -> {


            int frederik = database.createEmployee("Frederik Udby");
            int katinka = database.createEmployee("Katinka Spangtoft");
            int jens = database.createEmployee("Jens DinMor");
            int karsten = database.createEmployee("Karsten DinFar");
            int christian = database.createEmployee("Christian Vedel");
            int tobias = database.createEmployee("Tobias");
            int obama = database.createEmployee("Obama");

            int byg = database.createProject("Byg et tårn" ,"",session,"");
            database.addEmployeeToProject(byg,"frud",session);
            database.addEmployeeToProject(byg,"kasp",session);
            database.addEmployeeToProject(byg,"obam",session);

            int kage = database.createProject("Bag en kage","", session, "");
            database.addEmployeeToProject(kage,"chve",session);
            database.addEmployeeToProject(kage,"kasp",session);

            int find = database.createActivity(byg,"Find en byggeplads", 1,session);

            database.addEmployeeToActivity(byg,find,"kasp",session);
            database.addEmployeeToActivity(byg,find,"thor",session);

            database.createVacation("8","14",session);
            database.createSickLeave("kasp","2","",session);

        },Throwable::printStackTrace);
    }
}
