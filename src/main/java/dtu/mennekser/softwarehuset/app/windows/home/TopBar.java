package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TopBar extends HBox {

    TopBar() {
        Button logoutButton = new Button("Logout");
        getChildren().add(logoutButton);
        setAlignment(Pos.TOP_RIGHT);
        setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));

        logoutButton.setOnAction(actionEvent -> {
            ProjectApp.setScene(new LoginPage(), "Login");
        });
    }
}
