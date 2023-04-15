package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBTask;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewProjectWindow {
    private static boolean exists = false;

    public static void tryCreate(int employeeID) {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeProjectWindow = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        Scene newProjectScene = new Scene(root,300,400);
        makeProjectWindow.setScene(newProjectScene);

        root.getChildren().add(new Label("Project name:"));
        TextField nameField = new TextField();
        root.getChildren().add(nameField);

        Button create = new Button("Create");
        root.getChildren().add(create);

        create.setOnAction(actionEvent -> {
            String projectName = nameField.getText();
            DBTask.SubmitTask(database -> {
                int projectID = database.createProject(projectName);
                database.projects.get(projectID).assignEmployee(employeeID);

            });
            exists = false;
            makeProjectWindow.close();
        });

        makeProjectWindow.show();


        makeProjectWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
