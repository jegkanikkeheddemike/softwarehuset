package dtu.mennekser.softwarehuset.app.windows.login;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.windows.home.HomePage;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;

public class LoginPage extends VBox implements HasDBConnection {

    TextField usernameField = new TextField("");
    Label errorField = new Label("");
    Label availableField = new Label("Loading");
    ClientSubscriber<ArrayList<Employee>> employeesSubscriber;
    public LoginPage() {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(20));
        getChildren().add(new Label("Name:"));
        getChildren().add(usernameField);
        getChildren().add(errorField);
        getChildren().add(availableField);

        usernameField.setOnAction(actionEvent -> attemptLogin());


        employeesSubscriber = new DBSubscriber<>(
            database -> database.employees,
            employees -> {
                StringBuilder lines = new StringBuilder();
                lines.append("Available users:\n    ");
                employees.sort(Comparator.comparing(employee -> employee.name));
                for (Employee employee : employees) {
                    lines.append(employee.name).append("\n    ");
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
            ProjectApp.setScene(new Scene(new HomePage(),1920*0.5,1080*0.5), "Home");
        }
    }

    @Override
    public void cleanup() {
        employeesSubscriber.kill();
    }
}