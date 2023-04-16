package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class LeftMenu extends BorderPane {
    final DBSubscriber<ArrayList<Project>> projectSubscriber;

    LeftMenu() {
        VBox projectList = new VBox();
        setCenter(projectList);

        setBackground(Style.setBackground(1,0));
        setBorder(Style.setBorder(2,0,"right"));

        setPadding(new Insets(5,5,5,5));

        projectList.setSpacing(5);

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
                    projectList.getChildren().clear();
                    Label title = new Label("Mine Projekter");
                    title.setFont(Style.setTitleFont());
                    projectList.getChildren().add(title);
                    for (Project project : projects) {
                        Button button = new Button(project.name);
                        button.setOnAction(actionEvent -> HomePage.setProject(project));
                        Style.setProjectButtonStyle(button);
                        button.setStyle("-fx-text-fill: rgb(60,130,100)");
                        button.setAlignment(Pos.CENTER_LEFT);
                        projectList.getChildren().add(button);
                    }
                }
        );

        Button createProjectButton = new Button("New Project");
        Style.setProjectButtonStyle(createProjectButton);
        createProjectButton.setAlignment(Pos.CENTER);
        setBottom(createProjectButton);
        createProjectButton.setOnAction(actionEvent -> NewProjectWindow.tryCreate(HomePage.loggedInAs.id));

    }
}
