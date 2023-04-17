package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.networking.DBQuery;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class TopBar extends HBox {

    TopBar() {
        Button logoutButton = new Button("Logout");
        getChildren().add(logoutButton);
        setAlignment(Pos.TOP_RIGHT);
        setBorder(Style.setBorder(2,0,"bottom"));
        setBackground(Style.setBackground(1,0));
        setPadding(new Insets(5,5,5,5));

        Style.setBarButtonStyle(logoutButton, 60);

        logoutButton.setOnAction(actionEvent -> {
            ProjectApp.setScene(new LoginPage(), "Login");
        });

    }


}


