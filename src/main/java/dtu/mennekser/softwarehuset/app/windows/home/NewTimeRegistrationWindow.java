package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.*;
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
/**
 * @Author Frederik
 */
public class NewTimeRegistrationWindow extends VBox {

    public static boolean exists = false;
    DataListener<ArrayList<Project>> allProjectsListener;

    public static void tryCreate() {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeTimeRegistrationWindow = new Stage();
        NewTimeRegistrationWindow root = new NewTimeRegistrationWindow();
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newTimeRegistrationWindow = new Scene(root,300,400);
        makeTimeRegistrationWindow.setScene(newTimeRegistrationWindow);
        Session session = LoginManager.getCurrentSession();


        //---------- Title ---------------
        Label title = new Label("Register time:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");

        //------------------ ComboBox to choose a project and activity -------------
        ComboBox<String> projectName = new ComboBox<>();
        ComboBox<String> activityName = new ComboBox<>();

        //----------- Vbox that holds the activity selection menu ------------
        VBox activitySelection = new VBox();

        //----------- Error Field -----------------
        Label errorField = new Label("");

        //----------- Vbox that holds the register time button ------------
        VBox registerTimeArea = new VBox();
        registerTimeArea.setSpacing(5);

        Text timeUsedText = new Text("Time (hh:mm)");
        timeUsedText.setFont(Style.setTextFont());

        TextField timeUsedField = new TextField();
        Style.setTextField(timeUsedField,300);

        //----------- Register time button----------
        Button register = new Button("Register");
        Style.setActivityButtonStyle(register);
        register.setPrefSize(120,30);

        //set styling of the combo box
        projectName.setBackground(Style.setBackground(0, 5.0));
        projectName.setOnMouseEntered(actionEvent -> {
            projectName.setBackground(Style.setBackground(3, 5.0));
        });
        projectName.setOnMouseExited(actionEvent -> {
            projectName.setBackground(Style.setBackground(0, 5.0));
        });
        projectName.setPrefSize(300, 30);

        root.allProjectsListener = new DataListener<>(appBackend -> appBackend.getProjectsOfSession(session), projects -> {
            projectName.getItems().clear();

            for (Project project : projects) {
                projectName.getItems().add(project.name);
            }

            projectName.setOnAction(actionEvent -> {
                activitySelection.getChildren().clear();
                activitySelection.getChildren().addAll(new Label("Activity:"),activityName);
                Project selected = projects.stream().filter(project -> project.name.equals(projectName.getValue())).findFirst().get();
                activityName.getItems().addAll(selected.activities.stream().map(activity -> activity.name).toList());

                activityName.setOnAction(actionEvent2 -> {
                    Activity selectedActivity = selected.activities.stream().filter(activity -> activity.name.equals(activityName.getValue())).findFirst().get();
                    registerTimeArea.getChildren().clear();
                    registerTimeArea.getChildren().add(timeUsedText);
                    registerTimeArea.getChildren().add(timeUsedField);
                    registerTimeArea.getChildren().add(register);

                    register.setOnAction(actionEvent3 -> {
                        try {
                            String time = timeUsedField.getText();
                            AppBackend.assertValidTimeString(time);
                            DataTask.SubmitTask(appBackend -> appBackend.registerTime(selected.id,selectedActivity.id,time,session));
                            exists = false;
                            makeTimeRegistrationWindow.close();
                        } catch (Exception e) {
                            errorField.setText("Not valid time string");
                        }

                    });
                });
            });
        });

        activityName.setBackground(Style.setBackground(0, 5.0));
        activityName.setOnMouseEntered(actionEvent -> {
            activityName.setBackground(Style.setBackground(3, 5.0));
        });
        activityName.setOnMouseExited(actionEvent -> {
            activityName.setBackground(Style.setBackground(0, 5.0));
        });
        activityName.setPrefSize(300, 30);

        //--------- children in order -----------
        root.getChildren().add(title);
        root.getChildren().add(new Label("Project:"));
        root.getChildren().add(projectName);
        root.getChildren().add(activitySelection);
        root.getChildren().add(registerTimeArea);
        root.getChildren().add(errorField);


        makeTimeRegistrationWindow.show();
        makeTimeRegistrationWindow.setOnCloseRequest(windowEvent -> exists = false);
    }

}
