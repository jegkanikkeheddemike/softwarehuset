package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class HomePage extends Scene {
    static HomePage instance;

    BorderPane root;
    public HomePage() {
        super(new BorderPane(), 1920*0.5,1080*0.5);
        instance = this;

        root = (BorderPane) getRoot();
        root.setTop(new TopBar());
        root.setLeft(new LeftMenu());
        root.setCenter(new InnerHomePage());
    }

    public static void setProject(int projectID) {
        instance.root.setCenter(new ProjectMenu(projectID));
    }
    public static void setActivity(String projectName,int projectID, Activity activity) {
        instance.root.setCenter(new ActivityMenu(projectName,projectID, activity.id));
    }
    public static void setHome() {
        instance.root.setCenter(new InnerHomePage());
    }
}


class InnerHomePage extends BorderPane {

    DataListener<ArrayList<AppBackend.ActiveActivity>> activeActivities;
    InnerHomePage() {
        Session session = LoginManager.getCurrentSession();
        VBox rightMenu = new VBox();
        setMargin(rightMenu,new Insets(30));
        Label activeActivitesLabel = new Label("Active Activites");
        activeActivitesLabel.setFont(Style.setTitleFont());
        rightMenu.getChildren().add(activeActivitesLabel);
        setRight(rightMenu);

        VBox activeActivitiesBox = new VBox();

        ScrollPane activitiesScroll = new ScrollPane(activeActivitiesBox);

        rightMenu.getChildren().add(activitiesScroll);
        activeActivities = new DataListener<>(appBackend -> appBackend.getActiveActivities(session),
            activities -> {
                activeActivitiesBox.getChildren().clear();
                if (activities.size() == 0) {
                    activeActivitiesBox.getChildren().add(new Label("No activites :)"));
                    return;
                }

                for (AppBackend.ActiveActivity activity : activities) {
                    Button activityButton = new Button(activity.project().name + " / " + activity.activity().name);
                    activityButton.setOnAction(actionEvent -> HomePage.setActivity(activity.project().name,activity.project().id,activity.activity()));

                    activeActivitiesBox.getChildren().add(activityButton);
                }
            }
        );

        VBox leftMenu = new VBox();
        setMargin(leftMenu,new Insets(30));
        leftMenu.setSpacing(40);
        setCenter(leftMenu);

        Button registerTidButton = new Button("Registrer tid");
        Style.setButtonBig(registerTidButton);
        leftMenu.getChildren().add(registerTidButton);

        Button meldSygButton = new Button("Meld syg");
        Style.setButtonBig(meldSygButton);
        leftMenu.getChildren().add(meldSygButton);

        Button ferieButton = new Button("Ferie");
        Style.setButtonBig(ferieButton);
        leftMenu.getChildren().add(ferieButton);
    }
}