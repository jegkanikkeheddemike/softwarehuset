package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.login.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProjectApp extends Application {

    private static Pane root = new LoginPage();

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
    }

    public static void setRoot(Pane pane) {
        recursiveCleanup(root);
        root = pane;

    }

    private static void recursiveCleanup(Pane pane) {
        //Depth first
        for (Object child : pane.getChildren()) {
            if (child instanceof Pane) {
                recursiveCleanup((Pane) child);
            } else if (child instanceof HasDBConnection) {
                ((HasDBConnection) child).cleanup();
            }
        }
        if (pane instanceof HasDBConnection) {
            ((HasDBConnection) pane).cleanup();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
