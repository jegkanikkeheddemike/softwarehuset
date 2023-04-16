package dtu.mennekser.softwarehuset;

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

        Scene scene = new LoginPage();

        mainStage.setTitle("Login");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> System.exit(0));

    }

    public static void setScene(Scene newScene, String title) {
        Platform.runLater(() -> {
            mainStage.setTitle(title);
            mainStage.setScene(newScene);
        });
    }
}
