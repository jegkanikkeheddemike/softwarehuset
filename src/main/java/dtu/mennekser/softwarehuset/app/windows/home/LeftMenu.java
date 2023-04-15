package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LeftMenu extends VBox {
    LeftMenu() {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));



        DBSubscriber<ArrayList<Project>> projectSubscriber = new DBSubscriber<>(
                database -> {
                    ArrayList<Project> assigned = new ArrayList<>();
                    for (Project project : database.projects) {
                        if (project.assignedEmployees.contains(HomePage.loggedInAs.id)) {
                            assigned.add(project);
                        }
                    }
                    return assigned;
                }, projects -> {
                    getChildren().clear();
                    getChildren().add(new Label("Mine Projekter"));
                    for (Project project : projects) {

                        Button button = new Button(project.name);
                        getChildren().add(button);
                    }
                }

        );


    }
}
