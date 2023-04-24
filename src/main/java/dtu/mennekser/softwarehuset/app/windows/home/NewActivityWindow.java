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
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newActivityScene = new Scene(root,300,400);
        makeActivityWindow.setScene(newActivityScene);

        //-------------- Title ----------------------
        Label title = new Label("New Activity:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");
        root.getChildren().add(title);

        //--------------- Activity name -------------------
        root.getChildren().add(new Label("Activity name:"));
        TextField nameField = new TextField();
        Style.setTextField(nameField, 300);
        root.getChildren().add(nameField);

        //------------- Budgeted time -----------------
        root.getChildren().add(new Label("Budgeted time:"));
        TextField timeField = new TextField();
        Style.setTextField(timeField,300);
        root.getChildren().add(timeField);

        //------------- Start week ---------------------
        root.getChildren().add(new Label("Start week (optional)"));
        TextField startWeekField = new TextField();
        Style.setTextField(startWeekField,300);
        root.getChildren().add(startWeekField);

        //------------- End week ----------------------
        root.getChildren().add(new Label("End week (optional)"));
        TextField endWeekField = new TextField();
        Style.setTextField(endWeekField ,300);
        root.getChildren().add(endWeekField);

        //------------- create button ----------
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Button create = new Button("Create");
        Style.setActivityButtonStyle(create);
        create.setPrefSize(120,30);
        hBox.getChildren().add(create);
        root.getChildren().add(hBox);

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
