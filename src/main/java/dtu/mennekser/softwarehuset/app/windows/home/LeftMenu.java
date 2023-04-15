package dtu.mennekser.softwarehuset.app.windows.home;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LeftMenu extends VBox {
    LeftMenu() {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));

        getChildren().add(new Label("Mine Projekter"));

    }

    public Button ProjectButton(String name){
        Button pro = new Button(name);
        pro.setOnAction(e -> {
           //Open the project
        });
        return pro;
    }

}
