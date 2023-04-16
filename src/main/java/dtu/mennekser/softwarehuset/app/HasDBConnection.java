package dtu.mennekser.softwarehuset.app;
import javafx.scene.layout.Pane;

public interface HasDBConnection {
    public default void cleanup() {
        if (this instanceof Pane paneSelf) {
            killPane(paneSelf);
        } else {
            killSubscribers();
        }
    }

    private static void killPane(Pane pane) {
        for (Object child : pane.getChildren()) {
            if (child instanceof Pane paneChild) {
                killPane(paneChild);
            }

            if (child instanceof HasDBConnection) {
                ((HasDBConnection) child).cleanup();
            }
        }
        if (pane instanceof HasDBConnection paneHasDB) {
            paneHasDB.killSubscribers();
        }
    }

    public void killSubscribers();
}
