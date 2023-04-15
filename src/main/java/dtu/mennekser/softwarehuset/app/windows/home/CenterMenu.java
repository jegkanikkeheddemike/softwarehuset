package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class CenterMenu extends FlowPane implements HasDBConnection {
    Project project;


    DBSubscriber<ArrayList<Activity>> activitySubscriber;
    CenterMenu(Project project) {
        this.project = project;

        activitySubscriber = new DBSubscriber<>(
                database -> database.projects.get(project.id).activities,
                activities -> {
                    getChildren().clear();
                    Button newActivity = new Button("+");
                    getChildren().add(newActivity);
                    newActivity.setOnAction(actionEvent -> {
                        NewActivityWindow.tryCreate(project.id);
                    });


                    for (Activity activity : activities) {
                        Button button = new Button(activity.name);
                        getChildren().add(button);
                    }
                }
        );
    }

    @Override
    public void cleanup() {
        activitySubscriber.kill();
    }
}
