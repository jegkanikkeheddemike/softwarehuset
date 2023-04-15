package dtu.mennekser.softwarehuset.app.login;

import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class LoginPage extends VBox {

    TextField usernameField = new TextField("");
    Label errorField = new Label("");
    public LoginPage() {
        getChildren().add(new Label("Name:"));
        getChildren().add(usernameField);
        getChildren().add(errorField);

        usernameField.setOnAction(actionEvent -> attemptLogin());


        ClientSubscriber<ArrayList<Employee>> logsSubscriber = new ClientSubscriber<>(
                database -> database.employees,
                employees -> {
                    Platform.runLater(() -> {
                        StringBuilder lines = new StringBuilder();
                        for (Employee employee : employees) {
                            lines.append(employee.name).append("\n");
                        }
                        errorField.setText(lines.toString());
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
}