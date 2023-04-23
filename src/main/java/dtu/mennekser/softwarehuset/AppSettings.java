package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.app.windows.debug.DebugWindow;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientSettings;
import dtu.mennekser.softwarehuset.backend.streamDB.data.ServerListener;


/**
 * @author Thor
 */
public class AppSettings {

    //private static final String remoteLocation = "139.144.179.168";
    private static final String remoteLocation = "localhost";


    public static boolean debugMode = true;

    public static void init() {
        ClientSettings.remoteLocation = remoteLocation;
        if (debugMode) {
            new DebugWindow().show();
        }
    }
}