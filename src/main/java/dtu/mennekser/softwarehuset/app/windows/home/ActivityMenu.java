package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.networking.DBSubscriber;
import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ActivityMenu extends BorderPane {

    BorderPane assignedPane;
    VBox description;

    TextArea descriptionText;

    DBSubscriber<Activity> activitySubscriber;

    DBSubscriber<ArrayList<Employee>> assignedSubscriber;
    DBSubscriber<ArrayList<Employee>> notAssignedSubscriber;

    //Det her gør at hvis der kommer ændringer på projektet mens den er åben bliver det ikke reflekteret.
    //Men det er vel ok siden man ikke kan ændre på projektnavnet osv.
    //Måske kan det fikses men det er ok som det er indtil videre.

    public ActivityMenu(Project project,int activityID){
        activitySubscriber = new DBSubscriber<>(database -> database.projects.get(project.id).activities.get(activityID), activity -> {

            assignedPane = new BorderPane();
            description = new VBox();

            Text descriptionTitle = new Text("Description: ");
            descriptionTitle.setFont(Style.setTitleFont());

            if(!activity.description.isEmpty()) {
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
                    "-fx-highlight-fill: rgb(101,204,153);"+
                    "-fx-background-insets: 10;");


            descriptionText.setOnMouseEntered(mouseEvent -> {
                descriptionText.setStyle("-fx-control-inner-background: white;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-highlight-fill: rgb(101,204,153);"+
                        "-fx-background-insets: 10;");
            });
            descriptionText.setOnMouseExited(mouseEvent -> {
                descriptionText.setStyle(
                        "-fx-control-inner-background:  rgb(244, 244, 244);"+
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-highlight-fill: rgb(101,204,153);"+
                        "-fx-background-insets: 10;");
            });


            Button save = new Button("Save");
            save.setOnAction(actionEvent -> {
                String string = descriptionText.getText().trim();
                DBTask.SubmitTask( database -> {
                            database.projects.get(project.id).activities.get(activityID).setDescription(string);
                        }
                );
            });

            description.setPadding(new Insets(5,5,5,5));
            description.getChildren().addAll(descriptionTitle,descriptionText,save);

            setCenter(description);
            setRight(assignedPane);





            //--------------------------------------


            assignedSubscriber = new DBSubscriber<>(
                database -> {
                    ArrayList<Employee> assigned = new ArrayList<>();
                    for (int id : database.projects.get(project.id).activities.get(activityID).assigned) {
                        assigned.add(database.employees.get(id));
                    }
                    return assigned;
                }, employees -> {
                    VBox assignedList = new VBox();
                    assignedList.setSpacing(5);

                    assignedPane.getChildren().clear();
                    assignedPane.setCenter(assignedList);

                    //Create the buttons that show assigned employee
                    for (Employee employee: employees) {
                        Button employeeButton = new Button(employee.name);
                        Style.setEmployeeButtonStyle(employeeButton);
                        assignedList.getChildren().add(employeeButton);
                    }

                    HBox bottomMenu = new HBox();
                    assignedPane.setBottom(bottomMenu);

                    //DropDown menu for choosing who to add to an activity
                    ComboBox<String> employeeDropdown = new ComboBox<>();

                    employeeDropdown.setBackground(Style.setBackground(0,5.0));
                    employeeDropdown.setOnMouseEntered(actionEvent -> {
                        employeeDropdown.setBackground(Style.setBackground(3,5.0));

                    });
                    employeeDropdown.setOnMouseExited(actionEvent -> {
                        employeeDropdown.setBackground(Style.setBackground(0,5.0));

                    });
                    employeeDropdown.setPrefSize(130,30);

                    //set dropdown at bottom of employee overview
                    bottomMenu.getChildren().add(employeeDropdown);

                    notAssignedSubscriber = new DBSubscriber<>(database -> {
                        ArrayList<Employee> notAssigned = new ArrayList<>();
                        List<Integer> allEmployees = employees.stream().map(employee1 -> employee1.id).toList();
                        for (Employee employee : database.employees) {
                            if(database.projects.get(project.id).assignedEmployees.contains(employee.id)) {
                                if (!allEmployees.contains(employee.id)) {
                                    notAssigned.add(employee);
                                }
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
                        DBTask.SubmitTask(database -> database.projects.get(project.id).activities.get(activityID).assignEmployee(database.findEmployee(employeeName).id));
                    });


                }
            );
            //--------------------------------

            assignedPane.setBorder(Style.setBorder(3,0,"left"));
            assignedPane.setPadding(new Insets(5,5,5,5));
            assignedPane.setPrefWidth(120);

            setTop(new ActivityTopBar(project, activity));
        });

    }
}

