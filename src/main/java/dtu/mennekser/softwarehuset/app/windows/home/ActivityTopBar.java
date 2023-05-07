package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ActivityTopBar extends BorderPane {
    HBox left;
    HBox right;
    /**
     * @Author Thor
     */
    ActivityTopBar(String projectName, int projectID, Activity activity) {
        setBorder(Style.setBorder(3, 0, "bottom"));
        left = new HBox();
        right = new HBox();

        setPrefHeight(35);
        left.setAlignment(Pos.CENTER_LEFT);
        left.setSpacing(5);
        right.setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(0, 5, 0, 5));
        setLeft(left);
        setRight(right);

        Button backbutton = new Button("<");
        backbutton.setOnAction(actionEvent -> HomePage.setProject(projectID));
        left.getChildren().add(backbutton);

        Label title = new Label(projectName + "  ▶  " + activity.name);
        title.setFont(Style.setTitleFont());
        left.getChildren().add(title);

        if (!activity.finished) {
            Button finishButton = new Button("Set finished");
            Style.setBarButtonStyle(finishButton, 80);
            setMargin(finishButton, new Insets(20));
            left.getChildren().add(finishButton);
            finishButton.setOnAction(actionEvent -> {
                Session session = LoginManager.getCurrentSession();
                DataTask.SubmitTask(appBackend -> appBackend.finishActivity(projectID, activity.id, session));
            });
        } else {
            Label finished = new Label(" Finished ");
            finished.setFont(Style.setTitleFont());
            finished.setStyle("-fx-text-fill: rgb(54,174,123);");
            setMargin(finished,new Insets(20));
            left.getChildren().add(finished);
        }
    }
}
