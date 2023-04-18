package dtu.mennekser.softwarehuset.app.windows.debug;

import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.backend.schema.Log;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * @author Thor
 */
public class DebugScene extends Scene {
    DataListener<ArrayList<Log>> logListener;
    DataListener<Integer> activeListenersListener;

    public DebugScene(Parent parent) {
        super(parent, 340,500);
        Label activeConnections = new Label("");
        ((BorderPane) getRoot()).setTop(activeConnections);


        VBox content = new VBox();
        content.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(content);
        ((BorderPane) getRoot()).setCenter(scrollPane);

        logListener = new DataListener<>(database -> database.logs,
                logs -> {
                    content.getChildren().clear();
                    scrollPane.setVvalue(1D);
                    for (Log log : logs) {
                        content.getChildren().add(new Label(log.toString()));
                    }
                    scrollPane.setVvalue(1D);
                });

        activeListenersListener = new DataListener<>(
                database -> database.activeConnections,
                integer -> activeConnections.setText("Active connections: " + integer)
        );
    }
}
