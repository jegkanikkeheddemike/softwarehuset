package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProjectApp extends Application {



    static Stage mainStage;
    @Override
    public void start(Stage stage) throws Exception {
        ProjectSettings.init();

        mainStage = stage;

        Scene scene = new Scene(new LoginPage(), 320, 240);

        mainStage.setTitle("Login");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> System.exit(0));

    }

    public static void setScene(Scene newScene, String title) {
        Platform.runLater(() -> {
            mainStage.setTitle(title);
            recursiveCleanup((Pane) mainStage.getScene().getRoot());
            mainStage.setScene(newScene);
        });
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
