package dtu.mennekser.softwarehuset.app.windows.debug;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.backend.db.Log;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DebugWindow extends Stage {
    public DebugWindow() {

        BorderPane root = new BorderPane();
        Scene debugScene = new DebugScene(root);

        setScene(debugScene);


    }
}
