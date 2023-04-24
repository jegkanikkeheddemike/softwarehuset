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

public class NewSickLeaveWindow {
    public static boolean exists = false;

    public static void tryCreate() {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeSickLeaveWindow = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        Scene newActivityScene = new Scene(root,300,400);
        makeSickLeaveWindow.setScene(newActivityScene);

        root.getChildren().add(new Label("Sick Leave:"));

        root.getChildren().add(new Label("Start week"));
        TextField startWeekField = new TextField();
        root.getChildren().add(startWeekField);

        root.getChildren().add(new Label("Employee Name (optional)"));
        TextField employeeNameField = new TextField();
        root.getChildren().add(employeeNameField);

        Button create = new Button("Create");
        root.getChildren().add(create);

        Label errorField = new Label("");
        root.getChildren().add(errorField);


        create.setOnAction(actionEvent -> {
            Session session = LoginManager.getCurrentSession();
            String startWeek = startWeekField.getText().trim();


            String employeeName = employeeNameField.getText().trim().equals("") ? session.employee.name : employeeNameField.getText().trim();
            DataTask.SubmitTask(appBackend -> appBackend.createSickLeave(employeeName, startWeek, session));


            exists = false;
            makeSickLeaveWindow.close();
        });

        makeSickLeaveWindow.show();
        makeSickLeaveWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
