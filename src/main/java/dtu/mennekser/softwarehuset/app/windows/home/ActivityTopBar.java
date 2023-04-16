package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ActivityTopBar extends BorderPane {
    HBox left;
    HBox right;
    ActivityTopBar(Project project, Activity activity){
        setBorder(Style.setBorder(2,0,"bottom"));
        left = new HBox();
        right = new HBox();

        setPrefHeight(35);
        left.setAlignment(Pos.CENTER_LEFT);
        right.setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(0,5,0,5));
        setLeft(left);
        setRight(right);

        Label title = new Label(project.name +"  ▶  "+ activity.name);
        title.setFont(Style.setTitleFont());
        left.getChildren().add(title);

    }
}
