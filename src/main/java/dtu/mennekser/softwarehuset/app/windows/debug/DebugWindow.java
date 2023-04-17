package dtu.mennekser.softwarehuset.app.windows.debug;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Thor
 */
public class DebugWindow extends Stage {
    public DebugWindow() {

        BorderPane root = new BorderPane();
        Scene debugScene = new DebugScene(root);

        setScene(debugScene);


    }
}
