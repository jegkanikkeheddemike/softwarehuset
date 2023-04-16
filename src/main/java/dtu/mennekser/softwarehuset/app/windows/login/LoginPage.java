package dtu.mennekser.softwarehuset.app.windows.login;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.app.windows.home.HomePage;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;

public class LoginPage extends Scene {


    VBox root;
    TextField usernameField = new TextField("");
    Label errorField = new Label("");
    Label availableField = new Label("Loading");
    DBSubscriber<ArrayList<Employee>> employeesSubscriber;
    public LoginPage() {
        super(new VBox(),320, 260);
        root = (VBox) getRoot();
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(20));
        Label name = new Label("Name:");
        name.setFont(Style.setTitleFont());
        name.setStyle("-fx-text-fill: rgb(101,204,153);");
        root.getChildren().add(name);
        root.getChildren().add(usernameField);
        root.getChildren().add(errorField);
        root.getChildren().add(new Label("Available users:"));
        root.getChildren().add(new ScrollPane(availableField));

        usernameField.setBackground(Style.setBackground(0,5));
        usernameField.setFont(Style.setTextFont());
        usernameField.setStyle("-fx-highlight-fill: rgb(101,204,153);");
        usernameField.setOnAction(actionEvent -> attemptLogin());


        employeesSubscriber = new DBSubscriber<>(
            database -> database.employees,
            employees -> {
                StringBuilder lines = new StringBuilder();
                employees.sort(Comparator.comparing(employee -> employee.name));
                for (Employee employee : employees) {
                    lines.append(employee.name).append("\n");
                }
                availableField.setText(lines.toString());
            }
        );
    }

    void attemptLogin() {
        String username = usernameField.getText().trim();
        Employee employee = new DBQuery<Employee>(
                database -> database.findEmployee(username)
        ).fetch();

        if (employee == null) {
            errorField.setText("No such employee: " + username);
        } else {
            ProjectApp.setScene(new HomePage(employee),"Home");
        }
    }
}