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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewProjectWindow {
    private static boolean exists = false;

    public static void tryCreate() {
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
        TextField clientField = new TextField();
        TextField startField = new TextField();

        root.getChildren().add(nameField);
        root.getChildren().add(new Text("Client (optional)"));
        root.getChildren().add(clientField);
        root.getChildren().add(new Text("Start week (optional)"));
        root.getChildren().add(startField);

        Button create = new Button("Create");
        root.getChildren().add(create);


        Session session = LoginManager.getCurrentSession();
        create.setOnAction(actionEvent -> {
            String projectName = nameField.getText().trim();
            String clientName = clientField.getText().trim();
            String startWeek = startField.getText().trim();
            DataTask.SubmitTask(appBackend -> appBackend.createProject(projectName,clientName,session,startWeek));


            exists = false;
            makeProjectWindow.close();
        });

        makeProjectWindow.show();


        makeProjectWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
