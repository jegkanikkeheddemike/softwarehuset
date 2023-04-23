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

        Button create = new Button("Create");
        root.getChildren().add(create);

        Label errorField = new Label("");
        root.getChildren().add(errorField);


        create.setOnAction(actionEvent -> {
            String name = nameField.getText();
            Session session = LoginManager.getCurrentSession();
            int time = Integer.parseInt(timeField.getText().trim());
            DataTask.SubmitTask(appBackend -> appBackend.createActivity(projectId,name,time, session));

            exists = false;
            makeActivityWindow.close();
        });


        makeActivityWindow.show();


        makeActivityWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
