package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.networking.GarbageCollector;
import dtu.mennekser.softwarehuset.app.windows.login.LoginPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Thor
 */
public class AppMain extends Application {

    static Stage mainStage;
    @Override
    public void start(Stage stage) throws Exception {

        AppSettings.init();
        mainStage = stage;

        Scene scene = new LoginPage();

        mainStage.setTitle("Login");
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> System.exit(0));
        GarbageCollector.init();
    }

    public static void setScene(Scene newScene, String title) {
        Platform.runLater(() -> {
            mainStage.setTitle(title);
            mainStage.setScene(newScene);
            //Center main window
            for (Window window : Window.getWindows()) {
                if (window.getScene() == newScene) {
                    window.centerOnScreen();
                }
            }
        });
    }
}
