package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.windows.debug.DebugWindow;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSettings;

public class ProjectSettings {
    //private static final String remoteLocation = "koebstoffer.info";
    private static final String remoteLocation = "localhost";

    public static boolean debugMode = true;

    public static void init() {
        ClientSettings.remoteLocation = remoteLocation;
        if (debugMode) {
            new DebugWindow().show();
        }
    }
}
