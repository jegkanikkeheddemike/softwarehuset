package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newProjectScene = new Scene(root,300,400);
        makeProjectWindow.setScene(newProjectScene);


        //-------------- Title ----------------------
        Label title = new Label("New Project:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");
        root.getChildren().add(title);

        //------------- project name ------------------
        root.getChildren().add(new Label("Project name:"));
        TextField nameField = new TextField();
        Style.setTextField(nameField,300);
        root.getChildren().add(nameField);

        //------------- client (optional) --------------
        root.getChildren().add(new Text("Client (optional)"));
        TextField clientField = new TextField();
        Style.setTextField(clientField,300);
        root.getChildren().add(clientField);

        //------------------- Start week (optional) ----------
        root.getChildren().add(new Text("Start week (optional)"));
        TextField startField = new TextField();
        Style.setTextField(startField,300);
        root.getChildren().add(startField);


        //---------------- Create ---------------
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Button create = new Button("Create");
        Style.setActivityButtonStyle(create);
        create.setPrefSize(120,30);
        hBox.getChildren().add(create);
        root.getChildren().add(hBox);

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
