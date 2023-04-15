package dtu.mennekser.softwarehuset.app.windows;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Style {


    public static void setProjectButtonStyle(Button button){
        //all the settings for project buttons
        button.setFont(setTitleFont());
        button.setPrefSize(160, 40);
        button.setBackground(setBackground(1,2.0));
    }
    public static void setActivityButtonStyle(Button button){
        //all the settings for project buttons
        button.setFont(setTextFont());
        button.setPrefSize(100, 120);
        button.setBackground(setBackground(1,5.0));
    }


    public static Background setBackground(int theme, double corner){
        return new Background(new BackgroundFill(setTheme(theme), new CornerRadii(corner),null));
    }

    public static Color setTheme(int theme){
        if(theme == 1){
            return Color.LIGHTBLUE;
        } else if (theme==2) {
            return Color.CADETBLUE;
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
