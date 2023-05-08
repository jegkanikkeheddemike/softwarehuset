package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
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
/**
 * @Author Katinka
 */
public class NewSickLeaveWindow extends VBox {
    public static boolean exists = false;
    DataListener<ArrayList<Employee>> allEmployeeListener;
    public static void tryCreate() {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeSickLeaveWindow = new Stage();
        NewSickLeaveWindow root = new NewSickLeaveWindow();
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newActivityScene = new Scene(root,300,400);
        makeSickLeaveWindow.setScene(newActivityScene);

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

        root.getChildren().add(new Label("Employee Name (optional)"));
        ComboBox<String> employeeNameBox = new ComboBox<>();

        //set styling of the combo box
        employeeNameBox.setBackground(Style.setBackground(0, 5.0));
        employeeNameBox.setOnMouseEntered(actionEvent -> {
            employeeNameBox.setBackground(Style.setBackground(3, 5.0));
        });
        employeeNameBox.setOnMouseExited(actionEvent -> {
            employeeNameBox.setBackground(Style.setBackground(0, 5.0));
        });
        employeeNameBox.setPrefSize(300, 30);

        root.allEmployeeListener = new DataListener<>(
                AppBackend::getEmployees,
                employees -> {
                    employeeNameBox.getItems().clear();
                    employeeNameBox.getItems().add("");
                    employeeNameBox.getSelectionModel().select(0);
                    for (Employee employee : employees) {
                        employeeNameBox.getItems().add(employee.name);
                    }
                }
        );

        root.getChildren().add(employeeNameBox);

        //------------------- Create button -----------------------
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
            try {
                Session session = LoginManager.getCurrentSession();
                String startWeek = startWeekField.getText().trim();
                String endWeek = endWeekField.getText().trim();
                int begin = Integer.parseInt(startWeek);
                int end = Integer.parseInt(endWeek);
                if (begin < 1  || end > 52) {
                    throw new RuntimeException("");
                }

                String employeeName = employeeNameBox.getValue().equals("") ? session.employee.name : employeeNameBox.getValue();
                DataTask.SubmitTask(appBackend -> appBackend.createSickLeave(employeeName, startWeek, endWeek, session));

                exists = false;
                makeSickLeaveWindow.close();
            } catch (Exception e) {
                errorField.setText("Weeks must be between 1 and 52");
            }

        });

        makeSickLeaveWindow.show();
        makeSickLeaveWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
