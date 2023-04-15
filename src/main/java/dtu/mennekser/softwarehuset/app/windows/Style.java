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
        button.setBorder(setBorder(2,5, "nej"));
        button.setBackground(setBackground(1,5.0));
    }
    public static void setActivityButtonStyle(Button button){
        //all the settings for activity buttons
        button.setFont(setTextFont());
        button.setPrefSize(100, 120);
        button.setBackground(setBackground(1,5.0));
    }

    public static void setBarButtonStyle(Button button, double width){
        //all the settings for bar buttons
        button.setFont(setTextFont());
        button.setPrefSize(width, 20);
        button.setBorder(setBorder(2,2, "nej"));
        button.setBackground(setBackground(1,5.0));
    }

    public static Border setBorder(int theme, double corner, String edge){
        if(edge.equals("right")) {
            return new Border(new BorderStroke(
                    setTheme(theme), Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    new CornerRadii(corner), BorderWidths.DEFAULT,Insets.EMPTY));
        } else if(edge.equals("left")){

        } else if(edge.equals("top")){

        } else if(edge.equals("bottom")){

        }
        return new Border(new BorderStroke(setTheme(theme), BorderStrokeStyle.SOLID, new CornerRadii(corner), BorderWidths.DEFAULT));
    }
    public static Background setBackground(int theme, double corner){
        return new Background(new BackgroundFill(setTheme(theme), new CornerRadii(corner),null));
    }

    public static Color setTheme(int theme){
        if(theme == 1){
            return Color.rgb(101,204,153);
        } else if (theme == 2) {
            return Color.rgb(69,145,107);
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
