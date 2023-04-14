package dtu.mennekser.softwarehuset.app.login;

import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
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

        usernameField.setOnAction(actionEvent -> attempLogin());


        ClientSubscriber<ArrayList<Log>> logsSubscriber = new ClientSubscriber<>(
                "koebstoffer,info",
                database -> database.logs,
                logs -> errorField.setText(logs.toString()),
                Throwable::printStackTrace
        );
    }

    void attempLogin() {
        String username = usernameField.getText();
        Employee employee = new ClientQuery<Employee>(
                "koebstoffer.info",
                database -> database.findEmployee(username),
                Throwable::printStackTrace
        ).fetch();
        System.out.printf("Testing: " + employee);
        if (employee == null) {
            errorField.setText("No such employee");
        } else {
            errorField.setText("Yeah congratz not you can login :)");
        }
    }
}