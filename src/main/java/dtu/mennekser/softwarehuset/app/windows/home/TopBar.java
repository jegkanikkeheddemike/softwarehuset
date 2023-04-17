package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import dtu.mennekser.softwarehuset.backend.Business.LoginManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

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
            LoginManager.logout();
            ProjectApp.setScene(new LoginPage(), "Login");
        });

    }


}


