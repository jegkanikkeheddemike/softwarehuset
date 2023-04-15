package dtu.mennekser.softwarehuset.app.windows.home;

import dtu.mennekser.softwarehuset.app.windows.Style;
import dtu.mennekser.softwarehuset.backend.db.Project;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CenterTopBar extends HBox {
    CenterTopBar(Project project) {
        setBorder(Style.setBorder(2,0,"bottom"));
        Label title = new Label(project.name);
        title.setFont(Style.setTitleFont());
        getChildren().add(title);
    }
}
