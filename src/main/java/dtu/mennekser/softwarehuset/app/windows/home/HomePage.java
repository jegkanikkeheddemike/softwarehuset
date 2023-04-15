package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class HomePage extends Scene implements HasDBConnection {

    static Employee loggedInAs;

    BorderPane root;
    public HomePage(Employee loggedInAs) {
        super(new BorderPane(), 1920*0.5,1080*0.5);
        HomePage.loggedInAs = loggedInAs;
        root = (BorderPane) getRoot();
        root.setTop(new TopBar());
        root.setLeft(new LeftMenu());

    }

    @Override
    public void cleanup() {

    }
}
