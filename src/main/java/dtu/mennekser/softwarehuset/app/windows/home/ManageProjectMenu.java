package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.TimeManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ManageProjectMenu extends BorderPane {

    DataListener<AppBackend.ProjectStat> projectStatListener;

    public ManageProjectMenu(int projectID) {
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
                    weekLabel.setText(weekLabel.getText() + " (current week) ");
                    weekLabel.setTextFill(Color.RED);
                }
                if (i == projectStat.projectWeek()) {
                    weekLabel.setText(weekLabel.getText() + " (Project start) ");
                }


                gridPane.add(weekLabel, i, 0);
            }


            int minEmployeeHeight = 1;
            int currentMaxHeight = 1;

            for (AppBackend.EmployeeStat stat : projectStat.employeeStats()) {
                gridPane.add(new Label(stat.employee().name), 0, minEmployeeHeight);

                for (AppBackend.ActivityStat activity : stat.assignedActivities()) {
                    //Tjek om plades er omtaget
                    currentMaxHeight = Math.max(currentMaxHeight, insertAtOptimal(gridPane, activity, minEmployeeHeight, projectID));
                }
                currentMaxHeight += 1;
                minEmployeeHeight = currentMaxHeight;
            }
            if (!projectStat.unassignedActivities().isEmpty()) {
                gridPane.add(new Label("Unassigned:"), 0, minEmployeeHeight);
                for (Activity activity : projectStat.unassignedActivities()) {
                    insertAtOptimal(gridPane, new AppBackend.ActivityStat(projectID, "This value is ignored", activity), minEmployeeHeight, projectID);
                }
            }
        });


        gridScroll.setOnMouseClicked(mouseEvent -> {
            setBottomMenu(new ProjectTimeStats(projectID));
        });
        setBottomMenu(new ProjectTimeStats(projectID));

    }

    private int insertAtOptimal(GridPane gridPane, AppBackend.ActivityStat activity, int minEmployeeHeight, int projectID) {
        //Tjek om der er plads ved employeeHeight
        int minWeekCol = activity.activity().getStartWeek();
        int maxWeekCol = activity.activity().getEndWeek();

        int colSpan = maxWeekCol - minWeekCol + 1;

        //Move down until it finds empty space
        while (true) {
            Node blocking = getItemAtInterval(gridPane, minEmployeeHeight, minWeekCol, maxWeekCol);
            if (blocking == null) {
                break;
            }
            minEmployeeHeight += 1;
        }

        String buttonName;
        if (projectID == activity.projectID() || (activity.projectID() < 0)) {
            buttonName = activity.activity().name;
        } else {
            buttonName = activity.projectName() + " / " + activity.activity().name;
        }

        Button activityButton = new Button(buttonName);

        Background buttonBackground = new Background(new BackgroundFill(calcColor(activity.activity().name), CornerRadii.EMPTY, new Insets(0)));

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
        if (activity.projectID() == projectID) {
            activityButton.setOnAction(actionEvent -> setBottomMenu(new ActivityEditor(projectID, activity.activity().id)));
        } else if (activity.projectID() >= 0) {
            activityButton.setOnAction(actionEvent -> setBottomMenu(new Label("Cannot edit activity from other project")));
        } else {
            activityButton.setOnAction(actionEvent -> setBottomMenu(new Label("Cannot edit vacation / sick leave")));
        }
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
                childEndCol += childSpan - 1;
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

class ProjectTimeStats extends BorderPane {
    // Time used and time remaining for all activities in the current project should be displayed

    DataListener<Project> projectListener;
    ProjectTimeStats(int projectID){
        Session session = LoginManager.getCurrentSession();

        projectListener = new DataListener<>(appBackend -> appBackend.getProject(projectID, session), project -> {
            Label titleLabel = new Label(project.name +"/ all Activities");
            titleLabel.setFont(Style.setTitleFont());
            setTop(titleLabel);

            VBox timeStats = new VBox();
            Label budgetTime = new Label("Budgeted time: "+ project.getBudgetTime()/60+" h. "+ project.getBudgetTime()/60 + " m.");
            Label workedHours = new Label("Time used: " + project.getUsedTime()/60 +" h. "+ project.getUsedTime()%60 + " m.");
            Label remainingHours = new Label("Remaining time: " + project.remainingTime()/60 +" h. "+ project.remainingTime()%60 + " m.");

            timeStats.getChildren().addAll(budgetTime,workedHours,remainingHours);
            setCenter(timeStats);

        });

    }
}

class ActivityEditor extends BorderPane {

    DataListener<Activity> activityListener;
    DataListener<ArrayList<Employee>> assignedListener;
    DataListener<ArrayList<AppBackend.EmployeeNotAssignedToActivity>> notAssignedListener;

    ActivityEditor(int projectID, int activityID) {
        Session session = LoginManager.getCurrentSession();

        activityListener = new DataListener<>(appBackend -> appBackend.getActivity(projectID, activityID, session), activity -> {
            Label titleLabel = new Label(activity.name);
            titleLabel.setFont(Style.setTitleFont());
            setTop(titleLabel);

            VBox optionsBox = new VBox();
            Label optionsTitle = new Label("Time options");
            optionsTitle.setFont(Style.setTextFont());
            optionsBox.getChildren().add(optionsTitle);
            setLeft(optionsBox);

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
        assignedListener = new DataListener<>(appBackend -> appBackend.getAssignedActivityEmployees(projectID, activityID, session), employees -> {
            VBox assignedEmployeesBox = new VBox();
            setMargin(assignedEmployeesBox, new Insets(0, 20, 20, 20));
            setCenter(assignedEmployeesBox);

            Label title = new Label("Assigned employees");
            title.setFont(Style.setTextFont());
            assignedEmployeesBox.getChildren().add(title);

            VBox innerBox = new VBox();
            ScrollPane boxPane = new ScrollPane(innerBox);
            boxPane.setMaxWidth(300);
            assignedEmployeesBox.getChildren().add(boxPane);

            for (Employee employee : employees) {
                HBox employeeBox = new HBox();
                employeeBox.setSpacing(10);

                employeeBox.setAlignment(Pos.CENTER_LEFT);
                employeeBox.getChildren().add(new Label(employee.name));

                Button removeButton = new Button("Remove");
                removeButton.setOnAction(actionEvent -> DataTask.SubmitTask(appBackend -> appBackend.removeEmployeeFromActivity(projectID, activityID, employee.name, session)));

                employeeBox.getChildren().add(removeButton);

                innerBox.getChildren().add(employeeBox);
            }
        });

        notAssignedListener = new DataListener<>(appBackend -> appBackend.getEmployeesNotAssignedToActivity(projectID, activityID, session), employees -> {
            VBox notAssignedEmployeesBox = new VBox();
            setMargin(notAssignedEmployeesBox, new Insets(0, 20, 20, 20));
            setRight(notAssignedEmployeesBox);

            Label title = new Label("Assign employees");
            title.setFont(Style.setTextFont());
            notAssignedEmployeesBox.getChildren().add(title);

            VBox innerBox = new VBox();
            ScrollPane boxPane = new ScrollPane(innerBox);
            boxPane.setMaxWidth(300);
            notAssignedEmployeesBox.getChildren().add(boxPane);

            for (var employee : employees) {
                HBox employeeBox = new HBox();
                employeeBox.setSpacing(10);

                employeeBox.setAlignment(Pos.CENTER_LEFT);
                employeeBox.getChildren().add(new Label(employee.employee().name));
                if (employee.occupied()) {
                    Label occupiedLabel = new Label("Occupied");
                    occupiedLabel.setTextFill(Color.RED);
                    employeeBox.getChildren().add(occupiedLabel);
                } else {
                    Button removeButton = new Button("Assign");
                    removeButton.setOnAction(actionEvent -> DataTask.SubmitTask(appBackend -> appBackend.addEmployeeToActivity(projectID,activityID,employee.employee().name,session)));

                    employeeBox.getChildren().add(removeButton);
                }


                innerBox.getChildren().add(employeeBox);
            }
        });
    }
}
