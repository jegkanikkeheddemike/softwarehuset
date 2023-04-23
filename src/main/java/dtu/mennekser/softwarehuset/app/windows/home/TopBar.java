package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.AppMain;
import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class TopBar extends HBox {

    TopBar() {

        setAlignment(Pos.TOP_RIGHT);
        setBorder(Style.setBorder(2,0,"bottom"));
        setBackground(Style.setBackground(1,0));
        setPadding(new Insets(5,5,5,5));

        setSpacing(10);

        Button homeButton = new Button("Home");
        getChildren().add(homeButton);
        Style.setBarButtonStyle(homeButton, 60);

        homeButton.setOnAction(actionEvent -> HomePage.setHome());

        Button logoutButton = new Button("Logout");
        getChildren().add(logoutButton);
        Style.setBarButtonStyle(logoutButton, 60);

        logoutButton.setOnAction(actionEvent -> {
            LoginManager.logout();
            AppMain.setScene(new LoginPage(), "Login");
        });



    }


}


