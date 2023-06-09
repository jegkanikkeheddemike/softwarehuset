package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.networking.OnceQuery;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Project;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ProjectTopBar extends BorderPane {
    /**
     * @Author Tobias
     */
    HBox left;
    HBox right;

    ProjectTopBar(Project project) {
        setBorder(Style.setBorder(3, 0, "bottom"));
        left = new HBox();
        right = new HBox();

        setPrefHeight(35);
        setPadding(new Insets(0, 5, 0, 5));
        left.setAlignment(Pos.CENTER_LEFT);
        right.setAlignment(Pos.CENTER_RIGHT);
        setLeft(left);
        setRight(right);

        Label title = new Label(project.name + " (" + project.runningNumber + ")");
        Label client = new Label(" : " + project.client);
        Label startWeek = new Label(" - uge " + project.startWeek);
        client.setFont(Style.setTitleFont());
        title.setFont(Style.setTitleFont());
        startWeek.setFont(Style.setTextFont());
        left.getChildren().add(title);
        left.getChildren().add(client);
        left.getChildren().add(startWeek);

        Session session = LoginManager.getCurrentSession();

        Button setStart = new Button("Set start week");

        Style.setBarButtonStyle(setStart,100);
        TextField startField = new TextField();
        right.getChildren().add(startField);
        right.getChildren().add(setStart);

        setStart.setOnAction(ActionEvent -> {
            String newStartWeek = startField.getText().trim();
            DataTask.SubmitTask(appBackend -> appBackend.setStartTime(project.id,session,Integer.parseInt(newStartWeek)));
        });

        if (project.projectLeaderId == -1) {

            Button becomeProjectLeader = new Button("Become Project Leader");
            right.getChildren().add(becomeProjectLeader);
            Style.setBarButtonStyle(becomeProjectLeader, 165);

            becomeProjectLeader.setOnAction(actionEvent -> {
                DataTask.SubmitTask(appBackend -> appBackend.setProjectLeader(project.id, session));
            });
        } else {
            left.getChildren().add(new Label("(" + new OnceQuery<>(appBackend -> appBackend.getProjectLeader(project.id, session)).fetch().name + ")"));
        }
        if (project.projectLeaderId == session.employee.id) {
            Button statsButton = new Button("Manage project");
            statsButton.setOnAction(actionEvent -> {
                HomePage.setProjectStats(project.id);
            });
            right.getChildren().add(statsButton);
        }
    }
}
