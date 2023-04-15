package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class HomePage extends Scene implements HasDBConnection {

    BorderPane root;
    public HomePage() {
        super(new BorderPane(), 1920*0.5,1080*0.5);
        root = (BorderPane) getRoot();
        root.setTop(new TopBar());
    }

    @Override
    public void cleanup() {

    }
}
