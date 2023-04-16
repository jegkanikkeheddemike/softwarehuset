package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.HasDBConnection;
import dtu.mennekser.softwarehuset.backend.db.Activity;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class HomePage extends Scene implements HasDBConnection {
    static HomePage instance;
    static Employee loggedInAs;

    BorderPane root;
    public HomePage(Employee employee) {
        super(new BorderPane(), 1920*0.5,1080*0.5);
        instance = this;
        HomePage.loggedInAs = employee;

        root = (BorderPane) getRoot();
        root.setTop(new TopBar());
        root.setLeft(new LeftMenu());
        root.setCenter(new Label("Select a project to begin"));
    }

    public static void setProject(Project project) {
        if (instance.root.getCenter() != null && instance.root.getCenter() instanceof HasDBConnection) {
            ((HasDBConnection) instance.root.getCenter()).cleanup();
        }

        instance.root.setCenter(new CenterMenu(project.id));
    }
    public static void setActivity(Project project, Activity activity) {
        if (instance.root.getCenter() != null && instance.root.getCenter() instanceof HasDBConnection) {
            ((HasDBConnection) instance.root.getCenter()).cleanup();
        }

        instance.root.setCenter(new ActivityMenu(project, activity.id));
    }

    @Override
    public void killSubscribers() {
    }
}
