package dtu.mennekser.softwarehuset.app.windows;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Style {

    public static void setActivityButtonStyle(Button button){
        //all the settings for activity buttons
        button.setBackground(setBackground(1,5.0));
        button.setOnMouseEntered(actionEvent -> {
            button.setBackground(setBackground(2,5.0));
        });
        button.setOnMouseExited(actionEvent -> {
            button.setBackground(setBackground(1,5.0));
        });
        button.setFont(setTextFont());
        button.setPrefSize(100, 120);
    }
    public static void setProjectButtonStyle(Button button){
        //all the settings for project buttons
        setActivityButtonStyle(button);
        button.setFont(setTitleFont());
        button.setPrefSize(160, 40);
        //button.setBorder(setBorder(2,5, "nej"));
    }

    public static void setBarButtonStyle(Button button, double width){
        //all the settings for bar buttons
        setActivityButtonStyle(button);
        button.setFont(setTextFont());
        button.setPrefSize(width, 20);
        button.setBorder(setBorder(2,2, "nej"));
    }

    public static void setEmployeeButtonStyle(Button button){
        button.setBackground(setBackground(0,5.0));
        button.setOnMouseEntered(actionEvent -> {
            button.setBackground(setBackground(3,5.0));

        });
        button.setOnMouseExited(actionEvent -> {
            button.setBackground(setBackground(0,5.0));

        });
            button.setFont(setTextFont());
            button.setPrefSize(110, 20);

    }

    public static Border setBorder(int theme, double corner, String edge){
        if(edge.equals("right")) {
            return new Border(new BorderStroke(
                    setTheme(theme), setTheme(theme), setTheme(theme), setTheme(theme),
                    BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    new CornerRadii(corner), BorderWidths.DEFAULT,Insets.EMPTY));
        } else if(edge.equals("left")){
            return new Border(new BorderStroke(
                    setTheme(theme), setTheme(theme), setTheme(theme), setTheme(theme),
                    BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
                    new CornerRadii(corner), BorderWidths.DEFAULT,Insets.EMPTY));
        } else if(edge.equals("top")){
            return new Border(new BorderStroke(
                    setTheme(theme), setTheme(theme), setTheme(theme), setTheme(theme),
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    new CornerRadii(corner), BorderWidths.DEFAULT,Insets.EMPTY));
        } else if(edge.equals("bottom")){
            return new Border(new BorderStroke(
                    setTheme(theme), setTheme(theme), setTheme(theme), setTheme(theme),
                    BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    new CornerRadii(corner), BorderWidths.DEFAULT,Insets.EMPTY));
        }
        return new Border(new BorderStroke(setTheme(theme), BorderStrokeStyle.SOLID, new CornerRadii(corner), BorderWidths.DEFAULT));
    }
    public static Background setBackground(int theme, double corner){
        return new Background(new BackgroundFill(setTheme(theme), new CornerRadii(corner),null));
    }

    public static Color setTheme(int theme){
        if(theme == 0){
            return Color.WHITE;
        } else if(theme == 1){
            return Color.rgb(101,204,153);
        } else if (theme == 2) {
            return Color.rgb(69,145,107);
        } else if (theme ==3) {
            return Color.LIGHTGREY;
        }
        return Color.RED;
    }


    public static Font setTitleFont(){
       return Font.font("Verdana", FontWeight.BOLD, 15);
    }

    public static Font setTextFont(){
        return Font.font("Verdana", FontWeight.NORMAL, 12);
    }





}
