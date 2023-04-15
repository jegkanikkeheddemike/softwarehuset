package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.ProjectApp;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import javafx.geometry.Insets;
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
        setBorder(new Border(new BorderStroke(Style.setTheme(2),BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        setBackground(Style.setBackground(1,0));
        setPadding(new Insets(5,5,5,5));

        Style.setBarButtonStyle(logoutButton, 60);

        logoutButton.setOnAction(actionEvent -> {
            ProjectApp.setScene(new LoginPage(), "Login");
        });
    }
}
