package dtu.mennekser.softwarehuset.app.login;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

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


        employeesSubscriber = new ClientSubscriber<>(
                database -> database.employees,
                employees -> {
                    Platform.runLater(() -> {
                        System.out.println("Recieved employees");
                        StringBuilder lines = new StringBuilder();
                        lines.append("Available users:\n    ");
                        for (Employee employee : employees) {

                            lines.append(employee.name).append("\n    ");
                        }
                        availableField.setText(lines.toString());
                    });
                },
                Throwable::printStackTrace
        );
    }

    void attemptLogin() {
        String username = usernameField.getText();
        Employee employee = new ClientQuery<Employee>(
                database -> database.findEmployee(username),
                Throwable::printStackTrace
        ).fetch();
        Platform.runLater(()-> {
            if (employee == null) {
                errorField.setText("No such employee: " + username);
            } else {
                errorField.setText("Yeah congratz not you can login as " + username);
            }
        });
    }

    @Override
    public void cleanup() {
        employeesSubscriber.kill();
    }
}