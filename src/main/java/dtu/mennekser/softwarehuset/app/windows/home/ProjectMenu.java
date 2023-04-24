package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Project;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Comparator;

public class ProjectMenu extends BorderPane {

    FlowPane activitiesPane;
    BorderPane assignedPane;

    DataListener<Project> projectListener;
    DataListener<ArrayList<Employee>> assignedListener;
    DataListener<ArrayList<Employee>> notAssignedListener;

    String projectName;

    static Image checkmark;


    ProjectMenu(int projectID) {

        Session session = LoginManager.getCurrentSession();
        projectListener = new DataListener<>(appBackend -> appBackend.getProject(projectID, session),
            project -> {
                projectName = project.name;
                setTop(new ProjectTopBar(project));

                activitiesPane.getChildren().clear();
                Button newActivity = new Button("+");
                newActivity.setBackground(Style.setBackground(4,5.0));
                newActivity.setOnMouseEntered(actionEvent -> {
                    newActivity.setBackground(Style.setBackground(0,5.0));
                });
                newActivity.setOnMouseExited(actionEvent -> {
                    newActivity.setBackground(Style.setBackground(4,5.0));
                });
                newActivity.setFont(Font.font("Impact", FontWeight.BOLD, 40));
                newActivity.setStyle("-fx-text-fill: rgb(60,130,100)");
                newActivity.setPrefSize(100, 120);
                activitiesPane.getChildren().add(newActivity);
                newActivity.setOnAction(actionEvent -> {
                    NewActivityWindow.tryCreate(projectID);
                });

                //Sort activities to display unfinished first
                ArrayList<Activity> sortedActivities = new ArrayList<>(project.activities);
                sortedActivities.sort(Comparator.comparingInt(self -> (self.finished ? 1 : 0)));


                for (Activity activity : sortedActivities) {
                    StackPane buttonStack = new StackPane();
                    buttonStack.setAlignment(Pos.BOTTOM_RIGHT);
                    Button button = new Button(activity.name);
                    buttonStack.getChildren().add(button);
                    if (activity.finished) {
                        if (checkmark == null) {
                             checkmark = new Image("/checkmark.png");
                        }

                        ImageView imgview = new ImageView(checkmark);
                        imgview.setFitWidth(20);
                        imgview.setFitHeight(20);
                        buttonStack.getChildren().add(imgview);
                    }

                    Style.setActivityButtonStyle(button);
                    button.setOnAction(actionEvent -> HomePage.setActivity(projectName,projectID,activity));
                    activitiesPane.getChildren().add(buttonStack);


                    HBox bottomPane = new HBox();

                    setBottom(bottomPane);
                    Button setStart = new Button("Set start week");
                    bottomPane.getChildren().add(setStart);
                    Style.setBarButtonStyle(setStart,100);
                    TextField startField = new TextField();
                    bottomPane.getChildren().add(startField);

                    setStart.setOnAction(ActionEvent -> {
                        String startWeek = startField.getText().trim();
                        DataTask.SubmitTask(appBackend -> appBackend.setStartTime(projectID,session,Integer.parseInt(startWeek)));
                    });
                }
            }
        );


        activitiesPane = new FlowPane();
        assignedPane = new BorderPane();
        setCenter(activitiesPane);
        setRight(assignedPane);

        activitiesPane.setHgap(10);
        activitiesPane.setVgap(10);
        activitiesPane.setPadding(new Insets(5,5,5,5));


        assignedPane.setBorder(Style.setBorder(3,0,"left"));
        assignedPane.setPadding(new Insets(5,5,5,5));
        assignedPane.setPrefWidth(120);


        assignedPane.setMinWidth(180);
        assignedListener = new DataListener<>(
            appBackend -> appBackend.getAssignedEmployees(projectID, session),
            employees -> {
                VBox assignedList = new VBox();
                assignedList.setAlignment(Pos.TOP_CENTER);
                assignedList.setSpacing(5);

                assignedPane.setCenter(assignedList);

                for (Employee employee: employees) {
                    Button employeeButton = new Button(employee.name);
                    Style.setEmployeeButtonStyle(employeeButton);
                    assignedList.getChildren().add(employeeButton);
                }
            }
        );

        HBox bottomMenu = new HBox();
        bottomMenu.setSpacing(5);
        assignedPane.setBottom(bottomMenu);

        ComboBox<String> employeeDropdown = new ComboBox<>();

        employeeDropdown.setBackground(Style.setBackground(0,5.0));
        employeeDropdown.setOnMouseEntered(actionEvent -> {
            employeeDropdown.setBackground(Style.setBackground(3,5.0));

        });
        employeeDropdown.setOnMouseExited(actionEvent -> {
            employeeDropdown.setBackground(Style.setBackground(0,5.0));

        });


        employeeDropdown.setPrefSize(130,30);
        bottomMenu.getChildren().add(employeeDropdown);

        notAssignedListener = new DataListener<>(
            appBackend -> appBackend.getNotAssignedEmployees(projectID,session),
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
        addEmployee.setMinSize(30,30);

        bottomMenu.getChildren().add(addEmployee);
        addEmployee.setOnAction(actionEvent -> {
            String employeeName = employeeDropdown.getValue();
            DataTask.SubmitTask(appBackend -> appBackend.addEmployeeToProject(projectID, employeeName ,session));
        });
    }
}
