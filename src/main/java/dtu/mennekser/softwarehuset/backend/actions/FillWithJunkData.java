package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.AppSettings;
import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

public class FillWithJunkData {
    public static void main(String[] args) throws InterruptedException {

        //For at ClientTasks følger det der er valgt i AppSettings
        //skal AppSettings.init() køres.
        AppSettings.debugMode = false;
        AppSettings.init();

        new ClientTask<AppBackend>(appBackend -> appBackend.createEmployee("Thor Skipper"),Throwable::printStackTrace);

        Thread.sleep(500);
        LoginManager.attemptLogin("thsk");

        Session session = LoginManager.getCurrentSession();

        new ClientTask<AppBackend>(database -> {

            //Her kommer en lang list af kald der fylder serveren med noget data
            //Herunder både et sæt af udviklingsmedarbejdere
            //Samt nogle projekter og aktiviteter

            int frederik = database.createEmployee("Frederik Udby");
            int katinka = database.createEmployee("Katinka Spangtoft");
            int jens = database.createEmployee("Jens Petersen");
            int karsten = database.createEmployee("Karsten Sørnsen");
            int christian = database.createEmployee("Christian Vedel");
            int tobias = database.createEmployee("Tobias Martinsen");
            int obama = database.createEmployee("Barak Obama");
            int oliver = database.createEmployee("Oliver Hjælpelære");
            int gitte = database.createEmployee("Gitte Hansen");

            int byg = database.createProject("Byg et tårn" ,"",session,"");

            int find = database.createActivity(byg,"Find en byggeplads", 18,session);
            int mursten = database.createActivity(byg, "Køb mursten", 100, 12,15,session);
            int cement = database.createActivity(byg, "Bland cement", 129, 14,18,session);
            int fundament = database.createActivity(byg, "Byg fundament", 235, 11,19,session);

            database.addEmployeeToProject(byg,"frud",session);
            database.addEmployeeToProject(byg,"kasp",session);
            database.addEmployeeToProject(byg,"baob",session);
            database.addEmployeeToProject(byg,"kasø",session);
            database.addEmployeeToProject(byg,"olhj",session);
            database.addEmployeeToProject(byg,"giha",session);

            database.addEmployeeToActivity(byg,find,"kasp",session);
            database.addEmployeeToActivity(byg,find,"thsk",session);

            database.addEmployeeToActivity(byg,mursten,"frud",session);
            database.addEmployeeToActivity(byg,mursten,"kasp",session);

            database.addEmployeeToActivity(byg,cement,"frud",session);
            database.addEmployeeToActivity(byg,cement,"kasø",session);
            database.addEmployeeToActivity(byg,cement,"olhj",session);

            database.addEmployeeToActivity(byg,fundament,"thsk",session);
            database.addEmployeeToActivity(byg,fundament,"giha",session);


            int kage = database.createProject("Bag en kage","", session, "");

            int indgridienser = database.createActivity(kage, "Køb Indgridienser", 77, 36, 38, session);

            database.addEmployeeToProject(kage,"chve",session);
            database.addEmployeeToProject(kage,"kasp",session);
            database.addEmployeeToProject(kage,"giha",session);
            database.addEmployeeToProject(kage,"toma",session);
            database.addEmployeeToProject(kage,"olhj",session);

            database.addEmployeeToActivity(kage,indgridienser,"giha",session);
            database.addEmployeeToActivity(kage,indgridienser,"toma",session);


            database.createSickLeave("kasp","2","",session);
            database.createSickLeave("baob","27","28",session);
            database.createSickLeave("frud","1","5",session);
            database.createSickLeave("kasp","16","",session);


            database.createVacation("8","14",session);

            database.attemptLogin("kasp");
            database.createVacation("14","16",session);

            database.attemptLogin("frud");
            database.createVacation("8","14",session);

            database.attemptLogin("chve");
            database.createVacation("42","47",session);

            //Her sættes "Oliver Hjælpelære" til at være projekt leder af "Bag en kage"
            database.attemptLogin("olhj");
            database.setProjectLeader(kage,session);

        },Throwable::printStackTrace);
    }
}
