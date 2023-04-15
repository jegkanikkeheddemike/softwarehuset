package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.login.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProjectApp extends Application {

    private static StackPane root = new StackPane();
    private static LoginPage loginPage = new LoginPage();

    @Override
    public void start(Stage stage) throws Exception {
        setRoot(loginPage);

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(Pane pane) {
        root.getChildren().clear();

        root.getChildren().add(pane);
    }
    public static void main(String[] args) {
        launch();
    }
}
