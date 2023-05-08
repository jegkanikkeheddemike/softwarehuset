package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.ParseException;

public class ChangeWeekBoundsWindow {

    private static boolean exists = false;
    /**
     * @Author Christian
     */
    public static void tryCreate(int projectId, int activityId) {
        if (exists) {
            return;
        }
        exists = true;
        Stage changeWeekBoundsWindow = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        Scene changeWeekBoundsScene = new Scene(root,300,400);
        changeWeekBoundsWindow.setScene(changeWeekBoundsScene);

        root.getChildren().add(new Label("New start week"));
        TextField startWeekField = new TextField();
        root.getChildren().add(startWeekField);

        root.getChildren().add(new Label("New end week"));
        TextField endWeekField = new TextField();
        root.getChildren().add(endWeekField);

        Button save = new Button("Save");
        root.getChildren().add(save);

        Label errorField = new Label("");
        errorField.setWrapText(true);
        errorField.setMaxWidth(300);
        root.getChildren().add(errorField);

        save.setOnAction(actionEvent -> {
            try {
                Session session = LoginManager.getCurrentSession();
                int newStartWeek = Integer.parseInt(startWeekField.getText().trim());
                int newEndWeek = Integer.parseInt(endWeekField.getText().trim());
                if(newStartWeek < 1 || newStartWeek > 52 || newEndWeek < 0 || newEndWeek > 52) {
                    throw new RuntimeException("Invalid week bounds. Can't be less than 1 or greater than 52.");
                }
                DataTask.SubmitTask(appBackend -> appBackend.updateActivityWeekBounds(projectId,activityId,newStartWeek,newEndWeek,session));

                exists = false;
                changeWeekBoundsWindow.close();
            } catch (NumberFormatException e) {
              errorField.setText("Failed to parse string");
            } catch (Exception e) {
                errorField.setText(e.getMessage());
            }

        });

        changeWeekBoundsWindow.show();

        changeWeekBoundsWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
