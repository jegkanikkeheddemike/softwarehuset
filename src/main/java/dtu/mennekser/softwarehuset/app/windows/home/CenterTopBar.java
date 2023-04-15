package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CenterTopBar extends BorderPane {

    HBox left;
    HBox right;
    CenterTopBar(Project project) {
        setBorder(Style.setBorder(2,0,"bottom"));
        left = new HBox();
        right = new HBox();

        setLeft(left);
        setCenter(right);

        Label title = new Label(project.name);
        title.setFont(Style.setTitleFont());
        left.getChildren().add(title);

        if (project.projectLeaderId == -1) {
            Button becomeProjectLeader = new Button("Become Project Leader");
            left.getChildren().add(becomeProjectLeader);
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
