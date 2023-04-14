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


        ClientSubscriber<ArrayList<Log>> logsSubscriber = new ClientSubscriber<>(
                "koebstoffer.info",
                database -> database.logs,
                logs -> {
                    Platform.runLater(() -> {
                        String lines = "";
                        for (Log log : logs) {
                            lines  += log + "\n";
                        }
                        errorField.setText(lines);
                    });
                },
                Throwable::printStackTrace
        );

    }

    void attemptLogin() {
        String usernameTmp = " "+usernameField.getText();
        final String username = usernameTmp.trim();
        Employee employee = new ClientQuery<Employee>(
                "koebstoffer.info",
                database -> database.findEmployee(username),
                Throwable::printStackTrace
        ).fetch();
        Platform.runLater(()-> {
            System.out.printf("Testing: " + employee);
            if (employee == null) {
                errorField.setText("No such employee");
            } else {
                errorField.setText("Yeah congratz not you can login :)");
            }
        });

    }
}