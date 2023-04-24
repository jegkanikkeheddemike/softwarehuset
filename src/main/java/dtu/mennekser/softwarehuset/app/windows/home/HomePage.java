package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.app.TimeManager;
import dtu.mennekser.softwarehuset.app.networking.DataListener;
import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.schema.Activity;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;

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
    public static void setProjectStats(int projectID) {
        instance.root.setCenter(new ProjectStatMenu(projectID));
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
    DataListener<ArrayList<AppBackend.TimeRegisActivity>> registeredActivities;
    InnerHomePage() {
        Session session = LoginManager.getCurrentSession();
        //----------------- Personal Menu ----------------
        VBox rightMenu = new VBox();
        setMargin(rightMenu,new Insets(30));

        Label welcome = new Label("Welcome " + session.employee.name);
        welcome.setFont(Style.setTitleFont());
        welcome.setStyle("-fx-text-fill: rgb(54,174,123);");
        rightMenu.getChildren().add(welcome);

        Label activeActivitesLabel = new Label("Active Activites");
        activeActivitesLabel.setFont(Style.setTitleFont());
        activeActivitesLabel.setBorder(Style.setBorder(1,0,"top"));
        rightMenu.getChildren().add(activeActivitesLabel);
        setRight(rightMenu);

        VBox activeActivitiesBox = new VBox();
        ScrollPane activitiesScroll = new ScrollPane(activeActivitiesBox);


        activitiesScroll.setStyle(
                "-fx-control-inner-background: rgb(255, 255, 255);" +
                        "-fx-faint-focus-color: transparent;" +
                       "-fx-focus-color: transparent;" +
                        "-fx-highlight-fill: rgb(101,204,153);"+
                        "-fx-background-insets: 10;"
        );


        rightMenu.getChildren().add(activitiesScroll);
        activeActivities = new DataListener<>(appBackend -> appBackend.getActiveActivities(session),
            activities -> {
                activeActivitiesBox.getChildren().clear();
                if (activities.size() == 0) {
                    activeActivitiesBox.getChildren().add(new Label("No activites :)"));
                    return;
                }
                activities.sort(Comparator.comparing(activeActivity -> activeActivity.activity().getStartWeek()));
                activities.sort(Comparator.comparing(activeActivity -> activeActivity.activity().getEndWeek()));

                int week = TimeManager.getWeek();

                for (AppBackend.ActiveActivity activity : activities) {

                    Button activityButton = new Button(activity.project().name + " / " + activity.activity().name);
                    activityButton.setFont(Style.setTextFont());
                    Style.setActivityButtonStyle(activityButton);
                    activityButton.setPrefSize(200,30);
                    HBox buttonBox = new HBox();
                    buttonBox.setSpacing(10);

                    activeActivitiesBox.setMargin(buttonBox, new Insets(5));
                    buttonBox.setAlignment(Pos.CENTER_LEFT);

                    buttonBox.getChildren().add(activityButton);
                    activityButton.setOnAction(actionEvent -> HomePage.setActivity(activity.project().name,activity.project().id,activity.activity()));

                    Label timeLabel = new Label( "Week " + activity.activity().getStartWeek() + " -> " + activity.activity().getEndWeek());
                    buttonBox.getChildren().add(timeLabel);

                    if (week > activity.activity().getEndWeek()) {
                        Label overdue = new Label("OVERDUE");
                        overdue.setTextFill(Color.RED);
                        buttonBox.getChildren().add(overdue);
                    }

                    activeActivitiesBox.getChildren().add(buttonBox);

                }
            }
        );

        Label week = new Label("This week's Time Registrations: ");
        week.setFont(Style.setTitleFont());
        week.setBorder(Style.setBorder(1,0,"top"));
        rightMenu.getChildren().add(week);

        VBox regisActivitiesBox = new VBox();
        ScrollPane regisActivitiesScroll = new ScrollPane(regisActivitiesBox);

        rightMenu.getChildren().add(regisActivitiesScroll);
        registeredActivities = new DataListener<>(appBackend -> appBackend.getTimeRegisActivity(session), timeRegisActivities -> {
            regisActivitiesBox.getChildren().clear();
            if (timeRegisActivities.size() == 0) {
                regisActivitiesBox.getChildren().add(new Label("No time registrations :)"));
                return;
            }

            for (AppBackend.TimeRegisActivity regisActivity : timeRegisActivities) {
                HBox timeBox  = new HBox();
                timeBox.setSpacing(10);

                Label timeLabel = new Label(regisActivity.projectName() + " / " + regisActivity.activityName() + " -> " + regisActivity.timeRegistration().usedTime/60 + "h. " + regisActivity.timeRegistration().usedTime%60 + "m. ");
                timeBox.getChildren().add(timeLabel);

                regisActivitiesBox.getChildren().add(timeBox);
            }

        }
        );

            //---------------- General Menu --------------
        VBox leftMenu = new VBox();
        setMargin(leftMenu,new Insets(30));
        leftMenu.setSpacing(40);
        setCenter(leftMenu);

        Button registerTidButton = new Button("Registrer tid");
        Style.setButtonBig(registerTidButton);
        leftMenu.getChildren().add(registerTidButton);

        Button sickLeaveButton = new Button("Sick Leave");
        Style.setButtonBig(sickLeaveButton);
        sickLeaveButton.setOnAction(actionEvent -> {
            NewSickLeaveWindow.tryCreate();
        });
        leftMenu.getChildren().add(sickLeaveButton);

        Button vacationButton = new Button("Vacation");
        Style.setButtonBig(vacationButton);
        vacationButton.setOnAction(actionEvent -> {
            NewVacationWindow.tryCreate();
        });
        leftMenu.getChildren().add(vacationButton);
    }
}