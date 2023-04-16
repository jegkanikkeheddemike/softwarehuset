package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CenterTopBar extends BorderPane {

    HBox left;
    HBox right;
    CenterTopBar(Project project) {
        setBorder(Style.setBorder(3,0,"bottom"));
        left = new HBox();
        right = new HBox();

        setPrefHeight(35);
        setPadding(new Insets(0,5,0,5));
        left.setAlignment(Pos.CENTER_LEFT);
        right.setAlignment(Pos.CENTER_RIGHT);
        setLeft(left);
        setRight(right);

        Label title = new Label(project.name);
        title.setFont(Style.setTitleFont());
        left.getChildren().add(title);

        if (project.projectLeaderId == -1) {
            Button becomeProjectLeader = new Button("Become Project Leader");
            right.getChildren().add(becomeProjectLeader);
            Style.setBarButtonStyle(becomeProjectLeader,180);

            becomeProjectLeader.setOnAction(actionEvent -> {
                Employee loggedInAs = HomePage.loggedInAs;
                DBTask.SubmitTask(database -> {
                    database.projects.get(project.id).projectLeaderId = loggedInAs.id;
                });
            });
        } else {
            left.getChildren().add(new Label("(" + new DBQuery<>(database -> database.employees.get(project.projectLeaderId)).fetch().name + ")"));
        }
    }
}
