package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class ActivityMenu extends BorderPane implements HasDBConnection {

    BorderPane assignedPane;
    DBSubscriber<Activity> activitySubscriber;



    //Det her gør at hvis der kommer ændringer på projektet mens den er åben bliver det ikke reflekteret.
    //Men det er vel ok siden man ikke kan ændre på projektnavnet osv.
    //Måske kan det fikses men det er ok som det er indtil videre.

    public ActivityMenu(Project project,int activityID){
        activitySubscriber = new DBSubscriber<>(database -> database.projects.get(project.id).activities.get(activityID), activity -> {

            assignedPane = new BorderPane();
            setRight(assignedPane);

            


            assignedPane.setBorder(Style.setBorder(2,0,"left"));
            assignedPane.setPadding(new Insets(5,5,5,5));
            assignedPane.setPrefWidth(120);

            setTop(new ActivityTopBar(project, activity));
        });

    }

    @Override
    public void cleanup() {
        activitySubscriber.kill();
    }
}
