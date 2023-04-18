package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.Business.ProjectManager;
import dtu.mennekser.softwarehuset.backend.Business.TimeRegistrationManager;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ActivityMenu extends BorderPane {

    BorderPane assignedPane;
    VBox description;
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
        activityCenter.setCenter(description);
        BorderPane timerPane = new BorderPane();
        activityCenter.setRight(timerPane);

        setMargin(activityCenter, new Insets(10));
        activityCenter.setBorder(Style.setBorder(1, 10, "all"));


        activityListener = new DataListener<>(database -> database.projects.get(projectID).activities.get(activityID), activity -> {
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
            save.setOnAction(actionEvent -> {
                String string = descriptionText.getText().trim();
                DataTask.SubmitTask(database -> {
                            database.projects.get(projectID).activities.get(activityID).setDescription(string);
                        }
                );
            });

            description.setPadding(new Insets(5, 5, 5, 5));
            description.getChildren().addAll(descriptionTitle, descriptionText, save);

            setTop(new ActivityTopBar(projectName, projectID, activity));
        });

        setCenter(activityCenter);
        setRight(assignedPane);

        //--------------------------------------

        assignedPane.setMinWidth(180);
        assignedListener = new DataListener<>(
                database -> {
                    ArrayList<Employee> assigned = new ArrayList<>();
                    for (int id : database.projects.get(projectID).activities.get(activityID).assigned) {
                        assigned.add(database.employees.get(id));
                    }
                    return assigned;
                }, employees -> {
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
            ProjectManager.getEmployeesNotAssignedToActivity(projectID, activityID),
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
            ProjectManager.addEmployeeToActivity(projectID,activityID,employeeName);
        });


        //--------------------------------


        BorderPane bottomBox = new BorderPane();
        timerPane.setBottom(bottomBox);

        Button addTimerButton = new Button("+");
        bottomBox.setRight(addTimerButton);

        TextField timerField = new TextField("");
        bottomBox.setCenter(timerField);


        addTimerButton.setOnAction(actionEvent -> {
            String timeStr = timerField.getText().trim();
            TimeRegistrationManager.registerTime(timeStr,projectID,activityID);
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
                TimeRegistrationManager.getTimeRegistrations(projectID,activityID),
                timeRegistrations -> {
                    timerBox.getChildren().clear();
                    for (TimeRegistration regis : timeRegistrations) {
                        timerBox.getChildren().add(new Label(regis.employeeID + " : " + regis.usedTime));
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

