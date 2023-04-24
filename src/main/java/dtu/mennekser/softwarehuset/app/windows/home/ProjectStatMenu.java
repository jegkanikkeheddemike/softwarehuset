package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.TimeManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;

public class ProjectStatMenu extends BorderPane {

    DataListener<AppBackend.ProjectStat> projectStatListener;

    public ProjectStatMenu(int projectID) {
        GridPane gridPane = new GridPane();
        ScrollPane gridScroll = new ScrollPane(gridPane);
        setMargin(gridScroll, new Insets(20));
        setCenter(gridScroll);
        gridPane.setGridLinesVisible(true); //Virker ikke?
        gridPane.setHgap(10);

        Session session = LoginManager.getCurrentSession();
        projectStatListener = new DataListener<>(appBackend -> appBackend.getProjectStats(projectID, session), projectStat -> {
            gridPane.getChildren().clear();
            gridPane.add(new Label("Week: "), 0, 0);
            for (int i = 1; i <= 52; i++) {
                Label weekLabel = new Label(" " + i + " ");
                weekLabel.setMinWidth(30);
                if (i == TimeManager.getWeek()) {
                    weekLabel.setTextFill(Color.RED);
                }
                gridPane.add(weekLabel, i, 0);
            }


            int minEmployeeHeight = 1;
            int currentMaxHeight = 1;

            for (AppBackend.EmployeeStat stat : projectStat.employeeStats()) {
                gridPane.add(new Label(stat.employee().name), 0, minEmployeeHeight);

                for (Activity activity : stat.assignedActivities()) {
                    //Tjek om plades er omtaget
                    currentMaxHeight = Math.max(currentMaxHeight, insertAtOptimal(gridPane, activity, minEmployeeHeight, projectID));
                }
                minEmployeeHeight = currentMaxHeight + 1;
            }
            if (!projectStat.unassignedActivities().isEmpty()) {
                gridPane.add(new Label("Unassigned:"), 0, minEmployeeHeight);
                for (Activity activity : projectStat.unassignedActivities()) {
                    insertAtOptimal(gridPane, activity, minEmployeeHeight, projectID);
                }
            }
        });

        setBottomMenu(new Label("Click on an activity to edit"));

    }

    private int insertAtOptimal(GridPane gridPane, Activity activity, int minEmployeeHeight, int projectID) {
        //Tjek om der er plads ved employeeHeight
        int minWeekCol = activity.getStartWeek();
        int maxWeekCol = activity.getEndWeek();

        int colSpan = maxWeekCol - minWeekCol + 1;

        //Move down until it finds empty space
        while (true) {
            Node blocking = getItemAtInterval(gridPane, minEmployeeHeight, minWeekCol, maxWeekCol);
            if (blocking == null) {
                break;
            }
            minEmployeeHeight += 1;
        }

        Button activityButton = new Button(activity.name);

        Background buttonBackground = new Background(new BackgroundFill(calcColor(activity.name), CornerRadii.EMPTY, new Insets(0)));

        activityButton.setBackground(buttonBackground);
        activityButton.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(activityButton, minWeekCol, minEmployeeHeight, colSpan, 1);

        activityButton.hoverProperty().addListener(observable -> {
            if (activityButton.isHover()) {
                activityButton.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, new Insets(0))));
            } else {
                activityButton.setBackground(buttonBackground);
            }
        });
        activityButton.setOnAction(actionEvent -> setBottomMenu(new ActivityEditor(projectID, activity.id)));

        return minEmployeeHeight;
    }

    private static Node getItemAtInterval(GridPane gridPane, int row, int column, int endColumn) {
        for (Node child : gridPane.getChildren()) {

            Integer childRowObj = GridPane.getRowIndex(child);
            if (childRowObj == null) {
                continue;
            }
            int childRow = childRowObj;
            int childCol = GridPane.getColumnIndex(child);

            Integer childSpan = GridPane.getColumnSpan(child);
            int childEndCol = childCol;
            if (childSpan != null) {
                childEndCol += childSpan;
            }


            if (childRow != row) {
                continue;
            }
            //Check if child overlaps column + width
            if (within(column, childCol, endColumn) || within(column, childEndCol, endColumn)) {
                return child;
            }
            if (within(childCol, column, childEndCol) || within(childCol, endColumn, childEndCol)) {
                return child;
            }
        }
        return null;
    }

    private static boolean within(int min, int n, int max) {
        return min <= n && n <= max;
    }

    private static Color calcColor(String activityName) {
        int hash = Objects.hash(activityName);
        Random random = new Random(hash);
        int r = 256 / 2 + random.nextInt(256 / 2);
        int b = 256 / 2 + random.nextInt(256 / 2);
        int g = 256 / 2 + random.nextInt(256 / 2);
        return Color.rgb(r, g, b);

    }

    private void setBottomMenu(Node node) {
        BorderPane pane = new BorderPane();
        setMargin(pane, new Insets(0, 20, 20, 20));
        pane.setCenter(node);
        setBottom(pane);
    }
}

class ActivityEditor extends BorderPane {

    DataListener<Activity> activityListener;

    ActivityEditor(int projectID, int activityID) {
        Session session = LoginManager.getCurrentSession();

        activityListener = new DataListener<>(appBackend -> appBackend.getActivity(projectID, activityID, session), activity -> {
            Label titleLabel = new Label(activity.name);
            titleLabel.setFont(Style.setTitleFont());
            setTop(titleLabel);

            VBox optionsBox = new VBox();
            setCenter(optionsBox);

            HBox startWeek = new HBox();
            startWeek.setAlignment(Pos.CENTER_LEFT);
            optionsBox.getChildren().add(startWeek);

            startWeek.getChildren().add(new Label("Start week:"));
            TextField startWeekField = new TextField(Integer.toString(activity.getStartWeek()));
            startWeek.getChildren().add(startWeekField);

            HBox endWeek = new HBox();
            endWeek.setAlignment(Pos.CENTER_LEFT);
            optionsBox.getChildren().add(endWeek);

            endWeek.getChildren().add(new Label("End week:"));
            TextField endWeekField = new TextField(Integer.toString(activity.getEndWeek()));
            endWeek.getChildren().add(endWeekField);

            Button updateOptionsButton = new Button("Update");
            optionsBox.getChildren().add(updateOptionsButton);

            updateOptionsButton.setOnAction(actionEvent -> {
                int startWeekVal = Integer.parseInt(startWeekField.getText().trim());
                int endWeekVal = Integer.parseInt(endWeekField.getText().trim());
                if(startWeekVal < 1 || startWeekVal > 52 || endWeekVal < 0 || endWeekVal > 52) {
                    throw new RuntimeException("Invalid week bounds. Can't be less than 1 or greater than 52.");
                }
                DataTask.SubmitTask(appBackend -> appBackend.updateActivityWeekBounds(projectID,activityID,startWeekVal,endWeekVal,session));
            });

        });
    }
}
