module dtu.mennekser.softwarehusetas {
    requires javafx.controls;
    requires javafx.fxml;


    opens dtu.mennekser.softwarehusetas to javafx.fxml;
    exports dtu.mennekser.softwarehusetas;
}