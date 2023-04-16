package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CenterMenu extends BorderPane {

    FlowPane activitiesPane;
    BorderPane assignedPane;

    DBSubscriber<Project> projectSubscriber;
    DBSubscriber<ArrayList<Activity>> activitySubscriber;
    DBSubscriber<ArrayList<Employee>> assignedSubscriber;
    DBSubscriber<ArrayList<Employee>> notAssignedSubscriber;


    CenterMenu(int projectID) {
        projectSubscriber = new DBSubscriber<>(database -> database.projects.get(projectID), project -> {

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

            if (activitySubscriber != null) {
                activitySubscriber.kill();
            }

            activitySubscriber = new DBSubscriber<>(
                    database -> database.projects.get(project.id).activities,
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
                            NewActivityWindow.tryCreate(project.id);
                        });


                        for (Activity activity : activities) {
                            Button button = new Button(activity.name);
                            Style.setActivityButtonStyle(button);
                            //button.setStyle("-fx-text-fill: rgb(60,130,100)");
                            button.setOnAction(actionEvent -> HomePage.setActivity(project,activity));
                            activitiesPane.getChildren().add(button);
                        }
                    }
            );

            if (assignedSubscriber != null) {
                assignedSubscriber.kill();
            }

            assignedSubscriber = new DBSubscriber<>(
                    database -> {
                        ArrayList<Employee> assigned = new ArrayList<>();
                        for (int id : database.projects.get(project.id).assignedEmployees) {
                            assigned.add(database.employees.get(id));
                        }
                        return assigned;
                    }, employees -> {
                VBox assignedList = new VBox();
                assignedList.setSpacing(5);

                assignedPane.getChildren().clear();
                assignedPane.setCenter(assignedList);

                for (Employee employee: employees) {
                    Button employeeButton = new Button(employee.name);
                    Style.setEmployeeButtonStyle(employeeButton);
                    assignedList.getChildren().add(employeeButton);
                }

                HBox bottomMenu = new HBox();
                assignedPane.setBottom(bottomMenu);

                ComboBox<String> employeeDropdown = new ComboBox<>();

                employeeDropdown.setBackground(Style.setBackground(0,5.0));
                employeeDropdown.setOnMouseEntered(actionEvent -> {
                    employeeDropdown.setBackground(Style.setBackground(3,5.0));

                });
                employeeDropdown.setOnMouseExited(actionEvent -> {
                    employeeDropdown.setBackground(Style.setBackground(0,5.0));

                });


                employeeDropdown.setPrefSize(80,30);
                bottomMenu.getChildren().add(employeeDropdown);

                if (notAssignedSubscriber != null) {
                    notAssignedSubscriber.kill();
                }
                notAssignedSubscriber = new DBSubscriber<>(database -> {
                    ArrayList<Employee> notAssigned = new ArrayList<>();
                    List<Integer> allEmployees = employees.stream().map(employee1 -> employee1.id).toList();
                    for (Employee employee : database.employees) {
                        if (!allEmployees.contains(employee.id)) {
                            notAssigned.add(employee);
                        }
                    }
                    return notAssigned;
                }, notAssigned -> {
                    employeeDropdown.getItems().clear();
                    for (Employee employee : notAssigned) {
                        employeeDropdown.getItems().add(employee.name);
                    }
                });

                Button addEmployee = new Button("+");
                addEmployee.setFont(Style.setTextFont());
                Style.setEmployeeButtonStyle(addEmployee);
                addEmployee.setPrefSize(30, 30);

                bottomMenu.getChildren().add(addEmployee);
                addEmployee.setOnAction(actionEvent -> {
                    String employeeName = employeeDropdown.getValue();
                    DBTask.SubmitTask(database -> database.projects.get(project.id).assignEmployee(database.findEmployee(employeeName).id));
                });


            }
            );
            setTop(new CenterTopBar(project));
        });
    }
}
