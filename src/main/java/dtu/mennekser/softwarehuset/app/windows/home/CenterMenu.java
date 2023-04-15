package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CenterMenu extends BorderPane implements HasDBConnection {
    Project project;

    FlowPane activitiesPane;
    BorderPane assignedPane;


    DBSubscriber<ArrayList<Activity>> activitySubscriber;
    DBSubscriber<ArrayList<Employee>> assignedSubscriber;
    DBSubscriber<ArrayList<Employee>> notAssignedSubscriber;


    CenterMenu(Project project) {
        this.project = project;
        activitiesPane = new FlowPane();
        assignedPane = new BorderPane();
        setCenter(activitiesPane);
        setRight(assignedPane);

        activitySubscriber = new DBSubscriber<>(
                database -> database.projects.get(project.id).activities,
                activities -> {
                    activitiesPane.getChildren().clear();
                    Button newActivity = new Button("+");
                    activitiesPane.getChildren().add(newActivity);
                    newActivity.setOnAction(actionEvent -> {
                        NewActivityWindow.tryCreate(project.id);
                    });

                    for (Activity activity : activities) {
                        Button button = new Button(activity.name);
                        activitiesPane.getChildren().add(button);
                    }
                }
        );
        assignedSubscriber = new DBSubscriber<>(
                database -> {
                    ArrayList<Employee> assigned = new ArrayList<>();
                    for (int id : database.projects.get(project.id).assignedEmployees) {
                        assigned.add(database.employees.get(id));
                    }
                    return assigned;
                }, employees -> {
                    VBox assignedList = new VBox();

                    assignedPane.getChildren().clear();
                    assignedPane.setCenter(assignedList);

                    for (Employee employee: employees) {
                        Button employeeButton = new Button(employee.name);
                        assignedList.getChildren().add(employeeButton);
                    }

                    HBox bottomMenu = new HBox();
                    assignedPane.setBottom(bottomMenu);

                    ComboBox<String> employeeDropdown = new ComboBox<>();
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
                        for (Employee employee : notAssigned) {
                            employeeDropdown.getItems().add(employee.name);
                        }
                    });

                    Button addEmployee = new Button("+");
                    bottomMenu.getChildren().add(addEmployee);
                    addEmployee.setOnAction(actionEvent -> {
                        String employeeName = employeeDropdown.getValue();
                        DBTask.SubmitTask(database -> database.projects.get(project.id).assignEmployee(database.findEmployee(employeeName).id));
                    });

                }
        );

    }



    @Override
    public void cleanup() {
        activitySubscriber.kill();
    }
}
