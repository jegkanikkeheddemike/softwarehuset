module dtu.mennekser.softwarehuset {
    requires javafx.controls;
    requires javafx.fxml;

    opens dtu.mennekser.softwarehuset to javafx.fxml;
    exports dtu.mennekser.softwarehuset;
    exports dtu.mennekser.softwarehuset.app.windows.debug;
    opens dtu.mennekser.softwarehuset.app.windows.debug to javafx.fxml;
}