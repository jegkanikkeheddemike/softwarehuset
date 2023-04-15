package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LeftMenu extends VBox implements HasDBConnection {
    final DBSubscriber<ArrayList<Project>> projectSubscriber;
    LeftMenu() {
        setBackground(Style.setBackground(1,0));
        setBorder(Style.setBorder(2,0,"right"));
        //setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        setPadding(new Insets(5,5,5,5));
        setSpacing(5);

        Employee employee = HomePage.loggedInAs;
        projectSubscriber = new DBSubscriber<>(
                database -> {
                    ArrayList<Project> assigned = new ArrayList<>();
                    for (Project project : database.projects) {
                        if (project.assignedEmployees.contains(employee.id)) {
                            assigned.add(project);
                        }
                    }
                    return assigned;
                }, projects -> {
                    getChildren().clear();
                    Label title = new Label("Mine Projekter");
                    title.setFont(Style.setTitleFont());
                    getChildren().add(title);
                    for (Project project : projects) {
                        Button button = new Button(project.name);
                        button.setOnAction(actionEvent -> HomePage.setProject(project));
                        Style.setProjectButtonStyle(button);
                        getChildren().add(button);
                    }
                }
        );


    }

    @Override
    public void cleanup() {
        projectSubscriber.kill();
    }
}
