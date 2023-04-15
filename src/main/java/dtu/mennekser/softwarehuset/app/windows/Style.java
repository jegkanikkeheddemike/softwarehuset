package dtu.mennekser.softwarehuset.app.windows;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Style {


    public static void setProjectButtonStyle(Button button){
        //all the settings for project buttons
        button.setFont(setTitleFont());
        button.setPrefSize(160, 40);
        button.setBackground(new Background(new BackgroundFill(setTheme(1), new CornerRadii(15.0),null)));
    }

    public static Color setTheme(int theme){
        if(theme == 1){
            return Color.LIGHTBLUE;
        }
        return Color.RED;
    }


    public static Font setTitleFont(){
       return Font.font("Verdana", FontWeight.BOLD, 15);
    }

    public static Font setTextFont(){
        return Font.font("Verdana", FontWeight.NORMAL, 10);
    }





}
