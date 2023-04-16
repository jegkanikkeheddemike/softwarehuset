package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSettings;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProjectSettings {
    //private static final String remoteLocation = "koebstoffer.info";
    private static final String remoteLocation = "localhost";

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

        Label activeConnections = new Label("");
        root.setTop(activeConnections);


        Scene debugScene = new Scene(root,300,500);
        debugWindow.setScene(debugScene);

        debugWindow.show();

        ClientSubscriber<ArrayList<Log>> logSubscriber = new DBSubscriber<>(database -> database.logs,
            logs -> {
                content.getChildren().clear();
                scrollPane.setVvalue(1D);
                for (Log log : logs) {
                    content.getChildren().add(new Label(log.toString()));
                }
                scrollPane.setVvalue(1D);
            });

        ClientSubscriber<Integer> activeSubscribersSubscriber = new DBSubscriber<>(
                database -> database.activeConnections,
                integer -> activeConnections.setText("Active connections: " + integer)
        );


        debugWindow.setOnCloseRequest(windowEvent -> {
            logSubscriber.kill();
            activeSubscribersSubscriber.kill();
        });



    }
}
