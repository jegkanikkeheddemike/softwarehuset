package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.db.Project;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NewActivityWindow {
    public static boolean exists = false;


    public static void tryCreate(int projectId) {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeActivityWindow = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        Scene newActivityScene = new Scene(root,300,400);
        makeActivityWindow.setScene(newActivityScene);

        root.getChildren().add(new Label("Activity name:"));
        TextField nameField = new TextField();
        root.getChildren().add(nameField);

        root.getChildren().add(new Label("Budgeted time:"));
        TextField timeField = new TextField();
        root.getChildren().add(timeField);

        Button create = new Button("Create");
        root.getChildren().add(create);

        Label errorField = new Label("");
        root.getChildren().add(errorField);


        create.setOnAction(actionEvent -> {
            String name = nameField.getText();
            float time;
            try {
                time = Float.parseFloat(timeField.getText());
            } catch (Exception e) {
                errorField.setText("Time is not a number");
                return;
            }

            DBTask.SubmitTask(database -> {
                database.projects.get(projectId).createActivity(name,(int) time);
            });
            exists = false;
            makeActivityWindow.close();
        });


        makeActivityWindow.show();


        makeActivityWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
