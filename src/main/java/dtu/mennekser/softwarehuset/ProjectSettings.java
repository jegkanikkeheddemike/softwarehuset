package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSettings;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProjectSettings {
    private static String remoteLocation = "koebstoffer.info";
    //private static String remoteUrl = "localhost";

    public static boolean debugMode = true;

    public static void init() {
        ClientSettings.remoteLocation = remoteLocation;
        if (debugMode) {
            initDebugWindow();
        }
    }


    private static void initDebugWindow() {
        Stage debugWindow = new Stage();

        Pane root = new VBox();
        Scene debugScene = new Scene(root,200,400);
        debugWindow.setScene(debugScene);

        debugWindow.show();

        ClientSubscriber<ArrayList<Log>> logSubscriber = new ClientSubscriber<>(database -> database.logs,
            logs -> {
                root.getChildren().clear();
                for (Log log : logs) {
                    root.getChildren().add(new Label(log.toString()));
                }
            },
        Throwable::printStackTrace);

        debugWindow.setOnCloseRequest(windowEvent -> logSubscriber.kill());



    }
}
