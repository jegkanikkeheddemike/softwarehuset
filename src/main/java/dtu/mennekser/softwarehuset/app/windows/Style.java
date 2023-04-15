package dtu.mennekser.softwarehuset.app.windows;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Style {


    public static void setProjectButtonStyle(Button button){
        //all the settings for project buttons
        button.setFont(setTitleFont());
        button.setPrefSize(160, 40);

    }


    public static Font setTitleFont(){
       return Font.font("Verdana", FontWeight.BOLD, 15);
    }





}
