package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EditTimeRegistrationWindow {
    private static boolean exists = false;

    /**
     * @Author Katinka
     */
    static DataListener<ArrayList<AppBackend.TimeRegisActivity>> registeredActivities;
    public static void tryCreate() {
        if (exists) {
            return;
        }

        exists = true;
        Stage editTimeRegistrationWindow = new Stage();
        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newProjectScene = new Scene(root,300,400);
        editTimeRegistrationWindow.setScene(newProjectScene);

        Session session = LoginManager.getCurrentSession();

        Button change = new Button("Change");
        Style.setActivityButtonStyle(change);
        change.setPrefSize(120,30);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(change);

        TextField newTimeRegistration = new TextField();

        //-------------- Title ----------------------
        Label title = new Label("Edit Time Registration:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");
        root.getChildren().add(title);

        //------------- all "my" time registrations ------------------
        root.getChildren().add(new Label("Time registration:"));
        ComboBox<AppBackend.TimeRegisActivity> activityBox = new ComboBox<>();
        Label errorField = new Label("");


        registeredActivities = new DataListener<>(appBackend -> appBackend.getTimeRegisActivity(session), timeRegisActivities -> {
            activityBox.getItems().clear();
            if (timeRegisActivities.size() == 0) {
                //activityBox.getItems().add("No time registrations :)");
                return;
            }

            for (AppBackend.TimeRegisActivity regisActivity : timeRegisActivities) {
                activityBox.getItems().add(regisActivity);
            }

            change.setOnAction(actionEvent -> {
                try {
                    String[] timesplit = newTimeRegistration.getText().split(":");
                    int time = Integer.parseInt(timesplit[0])*60+Integer.parseInt(timesplit[1]);

                    AppBackend.TimeRegisActivity selected = activityBox.getValue();

                    DataTask.SubmitTask(appBackend -> appBackend.editTime(selected.projectID(), selected.activityID(),selected.timeRegistration().timeRegistrationID,time,session));
                    HomePage.setHome();
                    exists = false;
                    editTimeRegistrationWindow.close();
                } catch (Exception e) {
                    errorField.setText("Incorrect time format");
                }

            });

        }
        );

        root.getChildren().add(activityBox);


        root.getChildren().add(new Label("new time (hh:mm)"));
        root.getChildren().add(newTimeRegistration);
        root.getChildren().add(hBox);
        root.getChildren().add(errorField);


        editTimeRegistrationWindow.show();


        editTimeRegistrationWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
