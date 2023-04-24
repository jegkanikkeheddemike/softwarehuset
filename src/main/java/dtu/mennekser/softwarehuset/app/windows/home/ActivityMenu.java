package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;
import dtu.mennekser.softwarehuset.backend.streamDB.data.ServerListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ActivityMenu extends BorderPane {

    BorderPane assignedPane;
    VBox startEndWeekDisplay;
    VBox description;

    VBox center;
    BorderPane activityCenter;

    DataListener<Activity> activityListener;
    DataListener<ArrayList<Employee>> assignedListener;
    DataListener<ArrayList<Employee>> notAssignedListener;
    DataListener<ArrayList<TimeRegistration>> timerListener;

    //Det her gør at hvis der kommer ændringer på projektet mens den er åben bliver det ikke reflekteret.
    //Men det er vel ok siden man ikke kan ændre på projektnavnet osv.
    //Måske kan det fikses men det er ok som det er indtil videre.

    public ActivityMenu(String projectName, int projectID, int activityID) {
        assignedPane = new BorderPane();
        activityCenter = new BorderPane();
        description = new VBox();
        startEndWeekDisplay = new VBox();
        center = new VBox();
        activityCenter.setCenter(center);
        BorderPane timerPane = new BorderPane();
        activityCenter.setRight(timerPane);

        setMargin(activityCenter, new Insets(10));
        activityCenter.setBorder(Style.setBorder(1, 10, "all"));

        Session session = LoginManager.getCurrentSession();
        activityListener = new DataListener<>(
            appBackend -> appBackend.getActivity(projectID,activityID, session),
            activity -> {
                //initialise display for start and end week
                startEndWeekDisplay.getChildren().clear();
                Text startWeekText = new Text("Start Week: " + activity.getStartWeek());
                Text endWeekText = new Text("End Week: " + activity.getEndWeek());
                startWeekText.setFont(Style.setTitleFont());
                endWeekText.setFont(Style.setTitleFont());

                Button changeWeekBounds = new Button("Change Start and End Time");
                Style.setEmployeeButtonStyle(changeWeekBounds);
                changeWeekBounds.setMinSize(160,30);
                changeWeekBounds.setOnAction(actionEvent -> {
                    ChangeWeekBoundsWindow.tryCreate(projectID,activityID);
                });


                startEndWeekDisplay.getChildren().addAll(startWeekText,endWeekText,changeWeekBounds);



                //initialise description area
                startEndWeekDisplay.setPadding(new Insets(5, 5, 5, 5));
                description.getChildren().clear();
                Text descriptionTitle = new Text("Description: ");
                descriptionTitle.setFont(Style.setTitleFont());
                TextArea descriptionText;
                if (!activity.description.isEmpty()) {
                    descriptionText = new TextArea(activity.description);
                } else {
                    descriptionText = new TextArea();
                }
                descriptionText.setPromptText("Add description...");
                descriptionText.setFont(Style.setTextFont());
                descriptionText.setStyle(
                        "-fx-control-inner-background: rgb(244, 244, 244);" +
                                "-fx-faint-focus-color: transparent;" +
                                "-fx-focus-color: transparent;" +
                                "-fx-highlight-fill: rgb(101,204,153);" +
                                "-fx-background-insets: 10;");


                descriptionText.setOnMouseEntered(mouseEvent -> {
                    descriptionText.setStyle("-fx-control-inner-background: white;" +
                            "-fx-faint-focus-color: transparent;" +
                            "-fx-focus-color: transparent;" +
                            "-fx-highlight-fill: rgb(101,204,153);" +
                            "-fx-background-insets: 10;");
                });
                descriptionText.setOnMouseExited(mouseEvent -> {
                    descriptionText.setStyle(
                            "-fx-control-inner-background:  rgb(244, 244, 244);" +
                                    "-fx-faint-focus-color: transparent;" +
                                    "-fx-focus-color: transparent;" +
                                    "-fx-highlight-fill: rgb(101,204,153);" +
                                    "-fx-background-insets: 10;");
                });


                Button save = new Button("Save");
                Style.setEmployeeButtonStyle(save);
                save.setMinSize(45,30);

                save.setOnAction(actionEvent -> {
                    String newDescription = descriptionText.getText().trim();
                    DataTask.SubmitTask(appBackend -> {
                        appBackend.setDescription(projectID,activityID,newDescription,session);
                    });
                });

                description.setPadding(new Insets(5, 5, 5, 5));
                description.getChildren().addAll(descriptionTitle, descriptionText, save);

                setTop(new ActivityTopBar(projectName, projectID, activity));
            }
        );
        center.getChildren().addAll(startEndWeekDisplay,description);
        setCenter(activityCenter);
        setRight(assignedPane);

        assignedPane.setMinWidth(180);
        assignedListener = new DataListener<>(
            appBackend -> appBackend.getAssignedActivityEmployees(projectID,activityID,session),
            employees -> {
                VBox assignedList = new VBox();
                assignedList.setAlignment(Pos.TOP_CENTER);
                assignedList.setSpacing(5);

                assignedPane.setCenter(assignedList);

                //Create the buttons that show assigned employee
                for (Employee employee : employees) {
                    Button employeeButton = new Button(employee.name);
                    Style.setEmployeeButtonStyle(employeeButton);
                    assignedList.getChildren().add(employeeButton);
                }
            }
        );

        HBox bottomMenu = new HBox();
        bottomMenu.setSpacing(5);
        assignedPane.setBottom(bottomMenu);

        //DropDown menu for choosing who to add to an activity
        ComboBox<String> employeeDropdown = new ComboBox<>();

        employeeDropdown.setBackground(Style.setBackground(0, 5.0));
        employeeDropdown.setOnMouseEntered(actionEvent -> {
            employeeDropdown.setBackground(Style.setBackground(3, 5.0));

        });
        employeeDropdown.setOnMouseExited(actionEvent -> {
            employeeDropdown.setBackground(Style.setBackground(0, 5.0));

        });
        employeeDropdown.setPrefSize(130, 30);

        //set dropdown at bottom of employee overview
        bottomMenu.getChildren().add(employeeDropdown);

        notAssignedListener = new DataListener<>(
            appBackend -> appBackend.getEmployeesNotAssignedToActivity(projectID,activityID,session),
            notAssigned -> {
                employeeDropdown.getItems().clear();
                for (Employee employee : notAssigned) {
                    employeeDropdown.getItems().add(employee.name);
                }
            }
        );

        Button addEmployee = new Button("+");
        addEmployee.setFont(Style.setTextFont());
        Style.setEmployeeButtonStyle(addEmployee);
        addEmployee.setMinSize(30, 30);

        bottomMenu.getChildren().add(addEmployee);

        addEmployee.setOnAction(actionEvent -> {
            String employeeName = employeeDropdown.getValue();
            DataTask.SubmitTask(appBackend -> appBackend.addEmployeeToActivity(projectID,activityID, employeeName,session));
        });


        //------------------ Time Registration --------------


        BorderPane bottomBox = new BorderPane();
        timerPane.setBottom(bottomBox);

        Button addTimerButton = new Button("+");
        addTimerButton.setFont(Style.setTextFont());
        Style.setEmployeeButtonStyle(addTimerButton);
        addTimerButton.setMinSize(20, 20);
        bottomBox.setRight(addTimerButton);

        TextField timerField = new TextField("");
        timerField.setBackground(Style.setBackground(0,5));
        timerField.setPrefSize(107,20);
        bottomBox.setPadding(new Insets( 5));
        bottomBox.setLeft(timerField);


        addTimerButton.setOnAction(actionEvent -> {
            String timeStr = timerField.getText().trim();
            DataTask.SubmitTask(appBackend -> appBackend.registerTime(projectID,activityID,timeStr,session));
        });

        VBox timerBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(timerBox);
        scrollPane.setStyle(
                "-fx-control-inner-background: rgb(244, 244, 244);" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-highlight-fill: rgb(101,204,153);" +
                        "-fx-background-insets: 10;" +
                        "-fx-control-background: transparent;"
        );
        timerPane.setCenter(scrollPane);
        scrollPane.setBorder(new Border(new BorderStroke(Color.rgb(0, 0, 0, 0D), BorderStrokeStyle.NONE, new CornerRadii(10), BorderWidths.DEFAULT)));



        timerListener = new DataListener<>(
                appBackend -> appBackend.getTimeRegistrationsOfActivity(projectID,activityID,session),
                timeRegistrations -> {
                    timerBox.getChildren().clear();
                    for (TimeRegistration regis : timeRegistrations) {
                        timerBox.getChildren().add(new Label(regis.employeeID + " : " +regis.usedTime/60 +" h. "+ regis.usedTime%60 + " m."));
                    }
                }
        );

        timerPane.setMinWidth(150);
        timerPane.setBorder(Style.setBorder(3, 0, "left"));


        assignedPane.setBorder(Style.setBorder(3, 0, "left"));
        assignedPane.setPadding(new Insets(5, 5, 5, 5));
        assignedPane.setPrefWidth(120);


    }
}

