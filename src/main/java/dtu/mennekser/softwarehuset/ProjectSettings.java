package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSettings;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProjectSettings {
    //private static String remoteLocation = "koebstoffer.info";
    private static String remoteLocation = "localhost";

    public static boolean debugMode = true;

    public static void init() {
        ClientSettings.remoteLocation = remoteLocation;
        if (debugMode) {
            initDebugWindow();
        }
    }


    private static void initDebugWindow() {
        Stage debugWindow = new Stage();

        BorderPane root = new BorderPane();
        VBox content = new VBox();
        content.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(content);
        root.setCenter(scrollPane);


        Scene debugScene = new Scene(root,300,500);
        debugWindow.setScene(debugScene);

        debugWindow.show();

        ClientSubscriber<ArrayList<Log>> logSubscriber = new ClientSubscriber<>(database -> database.logs,
            logs -> {
                Platform.runLater(() -> {
                    content.getChildren().clear();
                    for (Log log : logs) {
                        content.getChildren().add(new Label(log.toString()));
                    }
                });
            },
        Throwable::printStackTrace);

        debugWindow.setOnCloseRequest(windowEvent -> logSubscriber.kill());



    }
}
