package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.Business.ProjectManager;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class CenterMenu extends BorderPane {

    FlowPane activitiesPane;
    BorderPane assignedPane;

    DataListener<Project> projectListener;
    DataListener<ArrayList<Activity>> activityListener;
    DataListener<ArrayList<Employee>> assignedListener;
    DataListener<ArrayList<Employee>> notAssignedListener;

    String projectName;


    CenterMenu(int projectID) {
        projectListener = new DataListener<>(database -> database.projects.get(projectID), project -> {
            projectName = project.name;
            setTop(new CenterTopBar(project));
        });


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

        activityListener = new DataListener<>(
                database -> database.projects.get(projectID).activities,
                activities -> {
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


                    for (Activity activity : activities) {
                        Button button = new Button(activity.name);
                        Style.setActivityButtonStyle(button);
                        button.setOnAction(actionEvent -> HomePage.setActivity(projectName,projectID,activity));
                        activitiesPane.getChildren().add(button);
                    }
                }
        );
        assignedPane.setMinWidth(180);
        assignedListener = new DataListener<>(
            ProjectManager.getAssignedEmployees(projectID), employees -> {
            VBox assignedList = new VBox();
            assignedList.setAlignment(Pos.TOP_CENTER);
            assignedList.setSpacing(5);

            assignedPane.setCenter(assignedList);

            for (Employee employee: employees) {
                Button employeeButton = new Button(employee.name);
                Style.setEmployeeButtonStyle(employeeButton);
                assignedList.getChildren().add(employeeButton);
            }
        });

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
                ProjectManager.getNotAssignedEmployees(projectID),
                notAssigned -> {
                    employeeDropdown.getItems().clear();
                    for (Employee employee : notAssigned) {
                        employeeDropdown.getItems().add(employee.name);
                    }
                });

        Button addEmployee = new Button("+");
        addEmployee.setFont(Style.setTextFont());
        Style.setEmployeeButtonStyle(addEmployee);
        addEmployee.setMinSize(30,30);

        bottomMenu.getChildren().add(addEmployee);
        addEmployee.setOnAction(actionEvent -> {
            String employeeName = employeeDropdown.getValue();
            ProjectManager.addEmployeeToProject(projectID,employeeName);
        });

    }
}
