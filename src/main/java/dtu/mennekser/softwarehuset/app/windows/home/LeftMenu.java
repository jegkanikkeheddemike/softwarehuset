package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Project;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class LeftMenu extends BorderPane {
    final DataListener<ArrayList<Project>> projectListener;

    LeftMenu() {
        VBox projectList = new VBox();
        setCenter(projectList);

        setBackground(Style.setBackground(1,0));
        setBorder(Style.setBorder(2,0,"right"));

        setPadding(new Insets(5,5,5,5));

        projectList.setSpacing(5);

        Session session = LoginManager.getCurrentSession();

        projectListener = new DataListener<>(
                appBackend -> appBackend.getProjectsOfSession(session),
                projects -> {
                    projectList.getChildren().clear();
                    Label title = new Label("My projects");
                    title.setFont(Style.setTitleFont());
                    projectList.getChildren().add(title);
                    for (Project project : projects) {
                        Button button = new Button(project.name + ": " + project.runningNumber);
                        button.setOnAction(actionEvent -> HomePage.setProject(project.id));
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
        createProjectButton.setOnAction(actionEvent -> NewProjectWindow.tryCreate());

    }
}
