module dtu.mennekser.softwarehuset {
    requires javafx.controls;
    requires javafx.fxml;


    opens dtu.mennekser.softwarehuset to javafx.fxml;
    exports dtu.mennekser.softwarehuset;
}