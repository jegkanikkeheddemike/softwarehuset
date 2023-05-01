package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Project;
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
        Scene newActivityScene = new Scene(root,300,400);
        makeTimeRegistrationWindow.setScene(newActivityScene);

        Label title = new Label("Sick Leave:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");
        root.getChildren().add(title);

        //----------------- TextField to Enter start week -----------------
        Text startWeekText = new Text("Start week");
        startWeekText.setFont(Style.setTextFont());
        root.getChildren().add(startWeekText);

        TextField startWeekField = new TextField();
        Style.setTextField(startWeekField,300);
        root.getChildren().add(startWeekField);

        //----------------- TextField to Enter end week -----------------
        Text endWeekText = new Text("End week (optional)");
        endWeekText.setFont(Style.setTextFont());
        root.getChildren().add(endWeekText);

        TextField endWeekField = new TextField();
        Style.setTextField(endWeekField,300);
        root.getChildren().add(endWeekField);

        //------------------ ComboBox to choose an employee (optional) ---------------------

        root.getChildren().add(new Label("Activity:"));
        ComboBox<String> activityName = new ComboBox<>();

        //set styling of the combo box
        activityName.setBackground(Style.setBackground(0, 5.0));
        activityName.setOnMouseEntered(actionEvent -> {
            activityName.setBackground(Style.setBackground(3, 5.0));
        });
        activityName.setOnMouseExited(actionEvent -> {
            activityName.setBackground(Style.setBackground(0, 5.0));
        });
        activityName.setPrefSize(300, 30);


        root.allProjectsListener = new DataListener<>(
                AppBackend::getEmployees,
                employees -> {
                    activityName.getItems().clear();
                    activityName.getItems().add("");
                    activityName.getSelectionModel().select(0);
                    for (Employee employee : employees) {
                        activityName.getItems().add(employee.name);
                    }
                }
        );

        root.getChildren().add(activityName);

        //------------------- Create button -----------------------
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Button register = new Button("Register");
        Style.setActivityButtonStyle(register);
        register.setPrefSize(120,30);
        hBox.getChildren().add(register);
        root.getChildren().add(hBox);

        Label errorField = new Label("");
        root.getChildren().add(errorField);

        register.setOnAction(actionEvent -> {
            // check who is logged in
            Session session = LoginManager.getCurrentSession();

            // get contents of dropdown menu


            exists = false;
            makeTimeRegistrationWindow.close();
        });

        makeTimeRegistrationWindow.show();
        makeTimeRegistrationWindow.setOnCloseRequest(windowEvent -> exists = false);
    }

}
