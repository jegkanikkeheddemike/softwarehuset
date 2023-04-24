package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        root.getChildren().add(new Label("Start week (optional)"));
        TextField startWeekField = new TextField();
        root.getChildren().add(startWeekField);

        root.getChildren().add(new Label("End week (optional)"));
        TextField endWeekField = new TextField();
        root.getChildren().add(endWeekField);

        Button create = new Button("Create");
        root.getChildren().add(create);

        Label errorField = new Label("");
        root.getChildren().add(errorField);


        create.setOnAction(actionEvent -> {
            String name = nameField.getText();
            Session session = LoginManager.getCurrentSession();
            int time = Integer.parseInt(timeField.getText().trim());


            int startWeek = startWeekField.getText().trim().equals("") ? 1 : Integer.parseInt(startWeekField.getText().trim());
            int endWeek = endWeekField.getText().trim().equals("") ? 52 : Integer.parseInt(endWeekField.getText().trim());
            if(startWeek < 1 || startWeek > 52 || endWeek < 0 || endWeek > 52) {
                throw new RuntimeException("Invalid week bounds. Can't be less than 1 or greater than 52.");
            }
            DataTask.SubmitTask(appBackend -> appBackend.createActivity(projectId,name,time,startWeek,endWeek, session));




            exists = false;
            makeActivityWindow.close();
        });


        makeActivityWindow.show();


        makeActivityWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
