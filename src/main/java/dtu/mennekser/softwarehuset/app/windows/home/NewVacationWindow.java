package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * @Author Katinka
 */
public class NewVacationWindow {
    public static boolean exists = false;

    public static void tryCreate() {
        if (exists) {
            return;
        }
        exists = true;
        Stage makeVacationWindow = new Stage();
        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10));
        Scene newActivityScene = new Scene(root,300,400);
        makeVacationWindow.setScene(newActivityScene);

        //-------------- Title ----------------------
        Label title = new Label("Vacation:");
        title.setFont(Style.setTitleFont());
        title.setStyle("-fx-text-fill: rgb(54,174,123);");
        root.getChildren().add(title);

        //-------------- Start week -----------------------
        root.getChildren().add(new Label("Start week"));
        TextField startWeekField = new TextField();
        Style.setTextField(startWeekField,300);
        root.getChildren().add(startWeekField);

        //----------------- End Week ----------------------
        root.getChildren().add(new Label("End week"));
        TextField endWeekField = new TextField();
        Style.setTextField(endWeekField,300);
        root.getChildren().add(endWeekField);

        //--------------- Create Button ------------------
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Button create = new Button("Create");
        Style.setActivityButtonStyle(create);
        create.setPrefSize(120,30);
        hBox.getChildren().add(create);
        root.getChildren().add(hBox);

        Label errorField = new Label("");
        root.getChildren().add(errorField);

        create.setOnAction(actionEvent -> {
            try {
                Session session = LoginManager.getCurrentSession();
                String startWeek = startWeekField.getText().trim();
                String endWeek = endWeekField.getText().trim();
                int begin = Integer.parseInt(startWeek);
                int end = Integer.parseInt(endWeek);
                if (begin < 1  || end > 52) {
                    throw new RuntimeException("");
                }
                DataTask.SubmitTask(appBackend -> appBackend.createVacation(startWeek,endWeek, session));


                exists = false;
                makeVacationWindow.close();
            } catch (Exception e) {
                errorField.setText("Weeks must be between 1 and 52");
            }


        });

        makeVacationWindow.show();
        makeVacationWindow.setOnCloseRequest(windowEvent -> exists = false);
    }
}
